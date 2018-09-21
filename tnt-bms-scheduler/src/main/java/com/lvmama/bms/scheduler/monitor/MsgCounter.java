package com.lvmama.bms.scheduler.monitor;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.cache.ShardHashKey;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.emnus.DataType;
import com.lvmama.bms.scheduler.monitor.emnus.StatisType;
import redis.clients.jedis.Pipeline;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MsgCounter {

    private RedisTemplate redisTemplate;

    private ExecutorService countThreadPool;

    private int TIME_PERIOD = 60;

    private int EXPIRE_DELAY = 30;

    private Logger logger = LoggerFactory.getLogger(MsgCounter.class);

    public MsgCounter(MsgSchedulerAppContext appContext) {
        redisTemplate = appContext.getRedisTemplate();
        countThreadPool = Executors.newFixedThreadPool(10, new NamedThreadFactory("MsgCounter"));
    }

    public void addReceiveCount(Long msgId, Integer msgTypeId, String... tokens) {
        count(msgId, msgTypeId, tokens, DataType.ReceiveCount);
    }

    public void addReachCount(Long msgId, Integer msgTypeId, String... tokens) {
        count(msgId, msgTypeId, tokens, DataType.ReachCount);
    }

    public void addFailCount(Long msgId, Integer msgTypeId, String... tokens) {
        count(msgId, msgTypeId, tokens, DataType.FailCount);
    }

    public void addSendCount(Long msgId, Integer msgTypeId, String... tokens) {
        count(msgId, msgTypeId, tokens, DataType.SendCount);
    }

    private void count(final Long msgId, final Integer msgTypeId, final String[] tokens, final DataType dataType) {
        try {
            countThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    redisTemplate.batch(new RedisTemplate.Batch() {
                        @Override
                        public List<Object> doBatch(Pipeline pipeline) {

                            Calendar calendar = Calendar.getInstance();
                            String suffix = String.valueOf(calendar.get(Calendar.MINUTE));
                            int expire = TIME_PERIOD + EXPIRE_DELAY;

                            String globalStatisKey = redisTemplate.getCacheKey(StatisType.GlobalStatis.name(), suffix, dataType.name());
                            pipeline.incrBy(globalStatisKey, tokens.length);
                            pipeline.expire(globalStatisKey, expire);

                            String msgTypeStatisKey = redisTemplate.getCacheKey(StatisType.MsgTypeStatis.name(), suffix, dataType.name(), String.valueOf(msgTypeId));
                            pipeline.incrBy(msgTypeStatisKey, tokens.length);
                            pipeline.expire(msgTypeStatisKey, expire);

                            for(String token : tokens) {
                                String tokenStatisKey = redisTemplate.getCacheKey(StatisType.TokenStatis.name(), suffix, dataType.name(), token);
                                pipeline.incr(tokenStatisKey);
                                pipeline.expire(tokenStatisKey, expire);
                            }

//                            String msgTokenStatisKey = redisTemplate.getCacheKey(StatisType.MsgTokenStatis.name(), suffix, dataType.name(), String.valueOf(msgId), String.valueOf(msgTypeId));
//                            pipeline.incrBy(msgTokenStatisKey, tokens.length);
//                            pipeline.expire(msgTokenStatisKey, expire);

                            return pipeline.syncAndReturnAll();

                        }
                    });
                }
            });
        } catch (Exception e) {
            logger.error("MsgCounter error", e);
        }
    }

}
