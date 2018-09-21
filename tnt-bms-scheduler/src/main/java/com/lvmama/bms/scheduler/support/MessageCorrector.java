package com.lvmama.bms.scheduler.support;

import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MessageStore;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgTokenPO;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 死亡消息修复
 */
public class MessageCorrector {

    private Logger logger = LoggerFactory.getLogger(MessageCorrector.class);

    private MsgTokenStore msgTokenStore;
    private MsgCacheManager msgCacheManager;

    private int LIVE_TIME = 3*60*1000;

    private int CHECK_TIME = 60*1000;

    public MessageCorrector(MsgSchedulerAppContext context) {
        msgTokenStore = context.getMsgTokenStore();
        msgCacheManager = context.getMsgCacheManager();
    }

    public void start() {

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("corrector-timer"));
        //每分钟执行一次
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            recoverDeathMsgToken();
        }

        }, CHECK_TIME, CHECK_TIME, TimeUnit.MILLISECONDS);

    }

    private void recoverDeathMsgToken() {

        try {
            //currentTime >= modifyTime + liveTime -> currentTime-liveTime >= modifyTime
            Calendar current = Calendar.getInstance();
            current.add(Calendar.MILLISECOND, -LIVE_TIME);
            for (MsgTypePO msgType : msgCacheManager.getMsgTypeOn()) {
                int recoverCount = msgTokenStore.recoverDeathMsgToken(MsgStatus.SENDING.ordinal(), MsgStatus.RECEIVE.ordinal(), current.getTime(), msgType.getId());
                logger.info("tip=start recover death message|msgType={}|recoverCount={}", msgType.getType(), recoverCount);
            }
        } catch (Exception e) {
            logger.error("tip=fail recover death message|exception", e);
        }

    }

}
