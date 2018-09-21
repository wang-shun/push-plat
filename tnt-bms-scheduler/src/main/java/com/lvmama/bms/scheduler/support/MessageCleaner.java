package com.lvmama.bms.scheduler.support;

import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MessageStore;
import com.lvmama.bms.scheduler.store.MsgPushFailStore;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageCleaner {

    private Logger logger = LoggerFactory.getLogger(MessageCleaner.class);

    private MsgCacheManager msgCacheManager;

    private MessageStore messageStore;

    private MsgPushFailStore msgPushFailStore;

    private ScheduledExecutorService clearTimer = Executors.newScheduledThreadPool(1);

    public MessageCleaner(MsgSchedulerAppContext appContext) {
        msgCacheManager = appContext.getMsgCacheManager();
        messageStore = appContext.getMessageStore();
        msgPushFailStore = appContext.getMsgPushFailStore();
    }

    public void start() {

        Calendar delay = Calendar.getInstance();
        Date current = delay.getTime();
        delay.add(Calendar.DAY_OF_MONTH, 1);
        delay.set(Calendar.HOUR_OF_DAY, 3);
        delay.set(Calendar.MINUTE, 0);

        clearTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                deleteExipredPushedMsg();

                deleteExpiredFailedMsg();

            }
        }, delay.getTimeInMillis() - current.getTime(), 24*60*60*1000, TimeUnit.MILLISECONDS);


    }

    /**
     * 清理tnt_bms_msg表中已发送消息
     */
    private void deleteExipredPushedMsg() {

        try {
            logger.info("tip=start delete expired pushed message");
            for(MsgTypePO msgType : msgCacheManager.getMsgTypeOn()) {
                messageStore.deleteExpiredMsg(msgType.getId());
            }
            logger.info("tip=finish delete expired pushed message");
        } catch (Exception e) {
            logger.error("tip=fail delete expired pushed message|exception=", e);
        }

    }

    /**
     * 清理tnt_bms_push_failure中留存时间超过3天，未做人工处理的消息
     */
    private void deleteExpiredFailedMsg() {

        try {
            logger.info("tip=start delete expired failed message");
            Calendar deadline = Calendar.getInstance();
            deadline.add(Calendar.DAY_OF_MONTH, -3); //current-pushTime>=3 -> current-3>=pushTime
            msgPushFailStore.deleteExpiredMsg(deadline.getTime());
            logger.info("tip=finish delete expired failed message");
        } catch (Exception e) {
            logger.error("tip=fail delete expired failed message|exception=", e);
        }

    }


}
