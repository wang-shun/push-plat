package com.lvmama.bms.scheduler.monitor.statis;

import com.lvmama.bms.alarm.email.EmailTableUtils;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.cache.ShardHashKey;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.emnus.DataType;
import com.lvmama.bms.scheduler.monitor.emnus.StatisType;
import com.lvmama.bms.scheduler.store.MsgTokenStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTokenStatisPO;
import com.lvmama.bms.scheduler.store.domain.po.statis.TokenStatisPO;
import com.lvmama.bms.scheduler.support.MsgCacheManager;
import redis.clients.jedis.Pipeline;

import java.util.*;

public class MsgTokenStatis {

    private MsgTokenStatisStore msgTokenStatisStore;

    private RedisTemplate redisTemplate;

    private MsgCacheManager msgCacheManager;

    private Float FAIL_RATE = 0.6F;

    private Config config;

    public MsgTokenStatis(MsgSchedulerAppContext appContext) {
        this.msgTokenStatisStore = appContext.getMsgTokenStatisStore();
        this.redisTemplate = appContext.getRedisTemplate();
        this.msgCacheManager = appContext.getMsgCacheManager();
        this.config = appContext.getConfig();
    }

    public Collection<MsgTokenStatisPO> statis(Calendar current) {

        String suffix = String.valueOf(current.get(Calendar.MINUTE));

        final List<String> keyList = new ArrayList<String>();
        final Set<String> keySet = redisTemplate.keys(redisTemplate.getCacheKey(StatisType.MsgTokenStatis.name(), suffix)+"*");
        List<Object> valueList = redisTemplate.batch(new RedisTemplate.Batch() {
            @Override
            public List<Object> doBatch(Pipeline pipeline) {
                for(Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
                    String key = iterator.next();
                    pipeline.get(key);
                    keyList.add(key);
                }
                return pipeline.syncAndReturnAll();
            }
        });

        Map<String, MsgTokenStatisPO> statisMap = new HashMap<String, MsgTokenStatisPO>();
        for(int index = 0; index < keyList.size(); index++) {

            String key = keyList.get(index);
            String[] keyParts = redisTemplate.getKeyParts(key);
            String msgId = keyParts[keyParts.length-2];
            Integer msgTypeId = Integer.parseInt(keyParts[keyParts.length-1]);

            MsgTokenStatisPO statis = statisMap.get(msgId);
            if (statis == null) {
                statis = new MsgTokenStatisPO();
                statis.setMsgId(msgId);
                statis.setMsgTypeId(msgTypeId);
                statis.setTimestamp(current.getTimeInMillis());
                statisMap.put(msgId, statis);
            }

            Integer value = Integer.parseInt((String) valueList.get(index));
            if (key.contains(DataType.FailCount.name())) {
                statis.setFailCount(value);
            } else if (key.contains(DataType.ReachCount.name())) {
                statis.setReachCount(value);
            } else if (key.contains(DataType.ReceiveCount.name())) {
                statis.setReceiveCount(value);
            } else if (key.contains(DataType.SendCount.name())) {
                statis.setSendCount(value);
            }
        }

        return statisMap.values();

    }

    public void store(Collection<MsgTokenStatisPO> msgTokenStatis) {
        for(MsgTokenStatisPO statis : msgTokenStatis) {
            msgTokenStatisStore.saveOrUpdate(statis);
        }
    }

    public String warn(Collection<MsgTokenStatisPO> msgTokenStatis) {

        List<List<Object>> warnInfo = new ArrayList<List<Object>>();

        for(MsgTokenStatisPO statis : msgTokenStatis) {
            if(statis.getFailCount()!=null && statis.getSendCount()!=null) {
                if(statis.getFailCount()*1F/statis.getSendCount() >= FAIL_RATE) {
                    MsgTypePO msgType = msgCacheManager.getMsgType(statis.getMsgTypeId());
                    if(msgType!=null) {
                        warnInfo.add(Arrays.asList((Object)
                                        statis.getMsgId(), msgType.getType(),
                                statis.getSendCount(), statis.getReachCount(),
                                statis.getFailCount()));
                    }
                }
            }
        }

        if(warnInfo.size() > 0) {
            return EmailTableUtils.createTable(
                    config.getParameter(ExtConfig.EMAIL_MSG_TOKEN_STATIS_TITLE),
                    config.getParameter(ExtConfig.EMAIL_MSG_TOKEN_STATIS_HEADER).split(","),
                    warnInfo);
        }

        return "";
    }



}
