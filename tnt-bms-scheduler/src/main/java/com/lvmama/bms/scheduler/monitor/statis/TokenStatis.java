package com.lvmama.bms.scheduler.monitor.statis;

import com.lvmama.bms.alarm.email.EmailTableUtils;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.cache.ShardHashKey;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.emnus.DataType;
import com.lvmama.bms.scheduler.monitor.emnus.StatisType;
import com.lvmama.bms.scheduler.store.TokenStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.TokenPO;
import com.lvmama.bms.scheduler.store.domain.po.statis.TokenStatisPO;
import com.lvmama.bms.scheduler.support.MsgCacheManager;
import redis.clients.jedis.Pipeline;

import java.util.*;

public class TokenStatis {

    private TokenStatisStore tokenStatisStore;

    private MsgCacheManager msgCacheManager;

    private RedisTemplate redisTemplate;

    private Config config;

    private Float FAIL_RATE = 0.6F;

    public TokenStatis(MsgSchedulerAppContext appContext) {
        this.tokenStatisStore = appContext.getTokenStatisStore();
        this.redisTemplate = appContext.getRedisTemplate();
        this.msgCacheManager = appContext.getMsgCacheManager();
        this.config = appContext.getConfig();
    }

    public Collection<TokenStatisPO> statis(Calendar current) {

        String suffix = String.valueOf(current.get(Calendar.MINUTE));

        final List<String> keyList = new ArrayList<String>();
        final Set<String> keySet = redisTemplate.keys(redisTemplate.getCacheKey(StatisType.TokenStatis.name(), suffix)+"*");
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

        Map<String, TokenStatisPO> statisMap = new HashMap<String, TokenStatisPO>();
        for(int index = 0; index < keyList.size(); index++) {

            String key = keyList.get(index);
            String[] keyParts = redisTemplate.getKeyParts(key);
            String token = keyParts[keyParts.length-1];

            TokenStatisPO statis = statisMap.get(token);
            if (statis == null) {
                statis = new TokenStatisPO();
                statis.setToken(token);
                statis.setTimestamp(current.getTimeInMillis());
                statisMap.put(token, statis);
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

    public int store(Collection<TokenStatisPO> tokenStatis) {
        return tokenStatisStore.batchSave(tokenStatis);
    }

    public String warn(Collection<TokenStatisPO> tokenStatis) {

        List<List<Object>> warnInfo = new ArrayList<List<Object>>();
        for(TokenStatisPO statis : tokenStatis) {
            TokenPO token = msgCacheManager.getToken(statis.getToken());
            if(token!=null && statis.getFailCount()!=null && statis.getSendCount()!=null) {
                if(statis.getFailCount()*1F/statis.getSendCount() >= FAIL_RATE) {
                    warnInfo.add(Arrays.asList(
                            (Object) statis.getToken(), token.getName(),
                            statis.getReceiveCount(), statis.getSendCount(),
                            statis.getReachCount(), statis.getFailCount()));
                }
            }
        }

        if(warnInfo.size() > 0) {
            return EmailTableUtils.createTable(
                    config.getParameter(ExtConfig.EMAIL_TOKEN_STATIS_TITLE),
                    config.getParameter(ExtConfig.EMAIL_TOKEN_STATIS_HEADER).split(","),
                    warnInfo);
        }

        return "";

    }


}
