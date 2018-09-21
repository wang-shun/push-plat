package com.lvmama.bms.scheduler.complete.biz.impl;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.commons.utils.StringUtils;
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

import java.util.List;
import java.util.concurrent.*;

public class PushSlowBiz implements PushBiz {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PushSlowBiz.class);

    /**
     * 慢消息判定比例阈值
     */
    private Float SLOW_RATIO_THRESHOLD = 0.6f;

    /**
     * 慢消息判定耗时阈值，毫秒
     */
    private Integer SLOW_TIME_THRESHOLD = 3 * 1000;

    /**
     * 判断周期，秒
     */
    private Integer STATIS_PERIOD = 60;

    /**
     * 缓存延时，秒
     */
    private int EXPIRE_DELAY = 5;

    private final String CACHE_PREFIX = "PushSlowBiz";

    private MsgCacheManager msgCacheManager;

    private ScheduledExecutorService bizTimer;

    private ExecutorService bizExecutor;

    private RedisTemplate redisTemplate;

    public PushSlowBiz(MsgSchedulerAppContext appContext) {
        this.msgCacheManager = appContext.getMsgCacheManager();
        this.redisTemplate = appContext.getRedisTemplate();
        this.bizExecutor = new ThreadPoolExecutor(60, 60,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(500));
        this.bizTimer = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("PushSlowBiz"));

    }

    @Override
    public RemotingCommand doBiz(final MsgPushedRequest request) {

        try {
            bizExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    List<PushResult> pushResults = request.getPushResults();
                    for(PushResult pushResult : pushResults) {
                        MessageDTO message = pushResult.getMessage();
                        monitorMsgSpeed(message.getMsgTypeId(), message.getToken(), pushResult.getTimeCost());
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error("tip=PushSlowBiz error|exception=", e);
        }

        return null;
    }

    private void monitorMsgSpeed(final Integer msgTypeId, final String token, long timeCost) {

        final String totalCountKey = redisTemplate.getCacheKey(CACHE_PREFIX, "totalCount", String.valueOf(msgTypeId), token);
        if(redisTemplate.increx(totalCountKey, STATIS_PERIOD+EXPIRE_DELAY) == 1) {
            bizTimer.schedule(new Runnable() {
                @Override
                public void run() {

                    String totalCountVal = redisTemplate.get(CACHE_PREFIX, "totalCount", String.valueOf(msgTypeId), token);
                    String slowCountVal = redisTemplate.get(CACHE_PREFIX, "slowCount", String.valueOf(msgTypeId), token);

                    if(StringUtils.isNotEmpty(totalCountVal)) {
                        slowCountVal = StringUtils.isEmpty(slowCountVal) ? "0" : slowCountVal;
                        if(Long.parseLong(slowCountVal)/Float.valueOf(totalCountVal) >= SLOW_RATIO_THRESHOLD) {
                            LOGGER.info("tip=discover slow message|msgTypeId={}|token={}", msgTypeId, token);
                            msgCacheManager.addSlowMessage(msgTypeId, token);
                        } else {
                            LOGGER.info("tip=recover slow message|msgTypeId={}|token={}", msgTypeId, token);
                            msgCacheManager.removeSlowMessage(msgTypeId, token);
                        }
                    }

                }
            }, STATIS_PERIOD, TimeUnit.SECONDS);
        }

        if(timeCost >= SLOW_TIME_THRESHOLD) {
            String slowCountKey = redisTemplate.getCacheKey(CACHE_PREFIX, "slowCount", String.valueOf(msgTypeId), token);
            redisTemplate.increx(slowCountKey, STATIS_PERIOD+EXPIRE_DELAY);
        }

    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
