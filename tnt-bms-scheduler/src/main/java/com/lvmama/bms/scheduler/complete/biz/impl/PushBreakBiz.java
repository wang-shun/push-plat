package com.lvmama.bms.scheduler.complete.biz.impl;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.PushResult;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.command.MsgPushedRequest;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.scheduler.complete.biz.PushBiz;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.support.MsgCacheManager;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.concurrent.*;

public class PushBreakBiz implements PushBiz {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PushBreakBiz.class);


    /**
     * 熔断失败比例阈值
     */
    private Float FAIL_RATE = 0.7f;

    /**
     * 判定周期，秒
     */
    private Integer STATIS_TIME = 60;

    /**
     * 恢复周期，秒
     */
    private Integer RECOVE_TIME = 45;

    /**
     * 缓存延时，秒
     */
    private Integer EXPIRE_DELAY = 10;

    private final String CACHE_PREFIX = "PushBreakBiz";

    private RedisTemplate redisTemplate;

    private MsgCacheManager msgCacheManager;

    private ScheduledExecutorService bizTimer;

    private ExecutorService bizExecutor;


    public PushBreakBiz(MsgSchedulerAppContext appContext) {
        redisTemplate = appContext.getRedisTemplate();
        msgCacheManager = appContext.getMsgCacheManager();
        bizTimer = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("PushBreakBiz"));
        bizExecutor = new ThreadPoolExecutor(30, 30,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(200));
    }

    @Override
    public RemotingCommand doBiz(final MsgPushedRequest request) {

        try {
            bizExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    for(PushResult result : request.getPushResults()) {
                        MessageDTO message = result.getMessage();
                        if(!msgCacheManager.isSuspendToken(message.getToken())) {
                            breakPush(message.getToken(), result.getAction());
                        }
                    }

                }
            });
        } catch (Exception e) {
            LOGGER.error("tip=PushBreakBiz error|exception=", e);
        }

        return null;

    }

    private void breakPush(final String token, Action action) {

        final String totalCountKey = redisTemplate.getCacheKey(CACHE_PREFIX, "totalCount", token);
        if(redisTemplate.increx(totalCountKey, STATIS_TIME+EXPIRE_DELAY) == 1) {
            bizTimer.schedule(new Runnable() {
                @Override
                public void run() {

                    String failCountVal = redisTemplate.get(CACHE_PREFIX, "failCount", token);
                    String totalCountVal = redisTemplate.get(CACHE_PREFIX, "totalCount", token);

                    if(StringUtils.isNotEmpty(failCountVal) && StringUtils.isNotEmpty(totalCountVal)) {
                        if (Float.valueOf(failCountVal) / Float.valueOf(totalCountVal) >= FAIL_RATE) {
                            LOGGER.warn("tip=break push|token="+token);
                            msgCacheManager.turnOffAndSyncToken(token, RECOVE_TIME);
                        }
                    }

                }
            }, STATIS_TIME, TimeUnit.SECONDS);
        }

        if(Action.EXECUTE_FAILED.equals(action) || Action.EXECUTE_EXCEPTION.equals(action)) {
            String failCountKey = redisTemplate.getCacheKey(CACHE_PREFIX, "failCount", token);
            redisTemplate.increx(failCountKey, STATIS_TIME+EXPIRE_DELAY);
        }

    }


    @Override
    public boolean isAsync() {
        return true;
    }
}
