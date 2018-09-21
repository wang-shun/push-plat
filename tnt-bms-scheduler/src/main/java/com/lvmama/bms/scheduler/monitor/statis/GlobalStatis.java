package com.lvmama.bms.scheduler.monitor.statis;

import com.lvmama.bms.alarm.email.EmailTableUtils;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.support.SystemClock;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.emnus.DataType;
import com.lvmama.bms.scheduler.monitor.emnus.StatisType;
import com.lvmama.bms.scheduler.store.GlobalStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.statis.GlobalStatisPO;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class GlobalStatis {

    private GlobalStatisStore globalStatisStore;

    private RedisTemplate redisTemplate;

    private Float FAIL_RATE = 0.6F;

    private Config config;

    public GlobalStatis(MsgSchedulerAppContext appContext) {
        this.globalStatisStore = appContext.getGlobalStatisStore();
        this.redisTemplate = appContext.getRedisTemplate();
        this.config = appContext.getConfig();
    }

    public GlobalStatisPO statis(Calendar current) {

        String suffix = String.valueOf(current.get(Calendar.MINUTE));

        GlobalStatisPO globalStatis = new GlobalStatisPO();
        String reachCount = redisTemplate.get(StatisType.GlobalStatis.name(), suffix, DataType.ReachCount.name());
        if(StringUtils.isNotEmpty(reachCount)) {
            globalStatis.setReachCount(Integer.parseInt(reachCount));
        }

        String receiveCount = redisTemplate.get(StatisType.GlobalStatis.name(), suffix, DataType.ReceiveCount.name());
        if(StringUtils.isNotEmpty(receiveCount)) {
            globalStatis.setReceiveCount(Integer.parseInt(receiveCount));
        }

        String sendCount = redisTemplate.get(StatisType.GlobalStatis.name(), suffix, DataType.SendCount.name());
        if(StringUtils.isNotEmpty(sendCount)) {
            globalStatis.setSendCount(Integer.parseInt(sendCount));
        }

        String failCount = redisTemplate.get(StatisType.GlobalStatis.name(), suffix, DataType.FailCount.name());
        if(StringUtils.isNotEmpty(failCount)) {
            globalStatis.setFailCount(Integer.parseInt(failCount));
        }

        globalStatis.setTimestamp(current.getTimeInMillis());

        return globalStatis;

    }

    public int store(GlobalStatisPO globalStatis) {
        return globalStatisStore.save(globalStatis);
    }

    public String warn(GlobalStatisPO globalStatis) {

        if(globalStatis.getFailCount()!=null && globalStatis.getSendCount()!=null) {
            if(globalStatis.getFailCount()*1F/globalStatis.getSendCount() >= FAIL_RATE) {
                List<Object> warnInfo = Arrays.asList(
                        (Object)globalStatis.getReceiveCount(), globalStatis.getSendCount(),
                        globalStatis.getReachCount(),globalStatis.getFailCount()
                );
                return EmailTableUtils.createTable(
                        config.getParameter(ExtConfig.EMAIL_GLOBAL_STATIS_TITLE),
                        config.getParameter(ExtConfig.EMAIL_GLOBAL_STATIS_HEADER).split(","),
                        Collections.singletonList(warnInfo));
            }
        }

        return "";

    }

}
