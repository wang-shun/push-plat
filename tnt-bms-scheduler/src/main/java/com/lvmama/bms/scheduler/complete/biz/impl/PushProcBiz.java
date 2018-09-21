package com.lvmama.bms.scheduler.complete.biz.impl;

import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.PushResult;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.command.MsgPushedRequest;
import com.lvmama.bms.core.store.mysql.MsgSerialize4DB;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.scheduler.complete.biz.PushBiz;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.MsgCounter;
import com.lvmama.bms.scheduler.store.MsgPushFailStore;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgPushFailPO;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.domain.po.TokenPO;
import com.lvmama.bms.scheduler.support.MsgCacheManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PushProcBiz implements PushBiz {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PushProcBiz.class);

    private MsgTokenStore msgTokenStore;

    private MsgPushFailStore msgPushFailStore;

    private MsgCounter msgCounter;

    private MsgCacheManager msgCacheManager;

    private Integer retryInterval;

    public PushProcBiz(MsgSchedulerAppContext appContext) {
        this.msgTokenStore = appContext.getMsgTokenStore();
        this.msgCounter = appContext.getMsgCounter();
        this.msgPushFailStore = appContext.getMsgPushFailStore();
        this.msgCacheManager = appContext.getMsgCacheManager();
        this.retryInterval = appContext.getConfig().getParameter(ExtConfig.PUSH_FAIL_RETRY_INTERVAL, 1);
    }

    @Override
    public RemotingCommand doBiz(MsgPushedRequest request) {

        for(PushResult pushResult : request.getPushResults()) {

            MessageDTO message = pushResult.getMessage();

            TokenPO tokenPO = msgCacheManager.getToken(message.getToken());
            if (tokenPO == null) {
                continue;//token 已被删除，忽略
            }

            MsgTypePO msgType = msgCacheManager.getMsgType(message.getMsgType());
            if(msgType == null) {
                continue; //消息类型已删除，忽略处理
            } else {
                message.setMsgTypeId(msgType.getId());
            }

            switch (pushResult.getAction()) {
                case EXECUTE_SUCCESS :
                    msgTokenStore.deleteMsgToken(message.getMsgTokenId(), message.getMsgTypeId());
                    break;
                case EXECUTE_EXCEPTION :
                    if(message.getRetryCount() < message.getMaxRetry()) { //失败重试
                        Calendar current = Calendar.getInstance();
                        current.add(Calendar.MINUTE, retryInterval);
                        msgTokenStore.updateRetryStatus(message.getMsgTokenId(), current.getTime(), message.getRetryCount()+1, MsgStatus.RECEIVE.ordinal(), message.getMsgTypeId());
                    } else { //转人工处理
                        saveFailMessage(message);
                    }
                    break;
                case EXECUTE_FAILED :
                    saveFailMessage(message); //转人工处理
                    break;
                case EXECUTE_RATE_LIMIT :
                    msgTokenStore.updateStatus(message.getMsgTokenId(), MsgStatus.RECEIVE.ordinal(), message.getMsgTypeId());
                    break;
            }
            LOGGER.info("tip=complete push message|" +
                    "action=" + pushResult.getAction() +
                    "|msg=" + pushResult.getMsg() +
                    "|timeCost=" + pushResult.getTimeCost() +
                    "|" + pushResult.getMessage().clearMsgContent()
                     );

            statis(message, pushResult.getAction());

        }

        return null;
    }

    private void statis(MessageDTO message, Action action) {

        if(Action.EXECUTE_SUCCESS.equals(action)) {
            msgCounter.addReachCount(message.getId(), message.getMsgTypeId(), message.getToken());
        } else if(Action.EXECUTE_EXCEPTION.equals(action) || Action.EXECUTE_FAILED.equals(action)) {
            msgCounter.addFailCount(message.getId(), message.getMsgTypeId(), message.getToken());
        }

        if(!Action.EXECUTE_RATE_LIMIT.equals(action)) {
            msgCounter.addSendCount(message.getId(), message.getMsgTypeId(), message.getToken());
        }

    }

    private void saveFailMessage(MessageDTO message) {

        MsgPushFailPO msgPushFailPO = new MsgPushFailPO();

        msgPushFailPO.setMaxRetry(message.getMaxRetry());
        msgPushFailPO.setMsgContent(MsgSerialize4DB.serialize(message.getMsgContent()));
        msgPushFailPO.setMsgId(message.getMsgId());
        msgPushFailPO.setMsgTypeId(message.getMsgTypeId());
        msgPushFailPO.setMsgType(message.getMsgType());
        msgPushFailPO.setToken(message.getToken());
        msgPushFailPO.setTokenId(msgCacheManager.getToken(message.getToken()).getId());
        msgPushFailPO.setReceiveTime(message.getReceiveTime());
        msgPushFailPO.setPushTime(new Date());

        msgPushFailStore.save(msgPushFailPO); //保存推送失败消息到tnt_bms_push_failure

        msgTokenStore.deleteMsgToken(message.getMsgTokenId(), message.getMsgTypeId());   //删除tnt_bms_msg_token数据

    }

    @Override
    public boolean isAsync() {
        return false;
    }

}
