package com.lvmama.bms.scheduler.monitor;

import com.lvmama.bms.alarm.AlarmNotifier;
import com.lvmama.bms.alarm.email.EmailAlarmMessage;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.support.SystemClock;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.statis.GlobalStatis;
import com.lvmama.bms.scheduler.monitor.statis.MsgTokenStatis;
import com.lvmama.bms.scheduler.monitor.statis.MsgTypeStatis;
import com.lvmama.bms.scheduler.monitor.statis.TokenStatis;
import com.lvmama.bms.scheduler.store.domain.po.statis.GlobalStatisPO;
import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTokenStatisPO;
import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTypeStatisPO;
import com.lvmama.bms.scheduler.store.domain.po.statis.TokenStatisPO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MsgStatis {

    private Logger logger = LoggerFactory.getLogger(MsgStatis.class);

    private ScheduledExecutorService statisTimer;

    private AlarmNotifier<EmailAlarmMessage> alarmNotifier;

    private GlobalStatis globalStatis;

    private TokenStatis tokenStatis;

    private MsgTokenStatis msgTokenStatis;

    private MsgTypeStatis msgTypeStatis;

    private Config config;

    public MsgStatis(MsgSchedulerAppContext appContext) {
        this.config = appContext.getConfig();
        this.alarmNotifier = appContext.getAlarmNotifier();
        this.globalStatis = new GlobalStatis(appContext);
        this.tokenStatis = new TokenStatis(appContext);
        this.msgTokenStatis = new MsgTokenStatis(appContext);
        this.msgTypeStatis = new MsgTypeStatis(appContext);
        this.statisTimer = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("MsgStatis"));
    }

    public void start() {

        //每分钟统计一次
        long delay = 60 - SystemClock.now()/1000 % 60;
        statisTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                Calendar current = Calendar.getInstance();
                current.set(Calendar.MILLISECOND, 0);
                current.add(Calendar.MINUTE, -1);

                try {
                    GlobalStatisPO globalStatisPO = globalStatis.statis(current);
                    Collection<TokenStatisPO> tokenStatisPO = tokenStatis.statis(current);
                    Collection<MsgTypeStatisPO> msgTypeStatisPO = msgTypeStatis.statis(current);
//                    Collection<MsgTokenStatisPO> msgTokenStatisPO = msgTokenStatis.statis(current);
                    if (globalStatisPO.checkData()) {
                        globalStatis.store(globalStatisPO);
                    }

                    if(tokenStatisPO.size() > 0) {
                        tokenStatis.store(tokenStatisPO);
                    }
                    if(msgTypeStatisPO.size() > 0) {
                        msgTypeStatis.store(msgTypeStatisPO);
                    }

//                    if(msgTokenStatisPO.size() > 0) {
//                        msgTokenStatis.store(msgTokenStatisPO);
//                    }

                    StringBuilder warnInfo = new StringBuilder();
                    warnInfo.append(globalStatis.warn(globalStatisPO));
                    warnInfo.append(tokenStatis.warn(tokenStatisPO));
                    warnInfo.append(msgTypeStatis.warn(msgTypeStatisPO));
//                    warnInfo.append(msgTokenStatis.warn(msgTokenStatisPO));

                    if(warnInfo.length() > 0) {
                        EmailAlarmMessage alarmMessage = new EmailAlarmMessage();
                        alarmMessage.setTitle(config.getParameter(ExtConfig.ALARM_EMAIL_TITLE));
                        alarmMessage.setTo(config.getParameter(ExtConfig.ALARM_EMAIL_TO));
                        alarmMessage.setMsg(warnInfo.toString());
                        alarmNotifier.notice(alarmMessage);
                    }

                } catch (Exception e) {
                    logger.error("statis data fail", e);
                }

            }
        }, delay*1000, 60*1000, TimeUnit.MILLISECONDS);

    }

}
