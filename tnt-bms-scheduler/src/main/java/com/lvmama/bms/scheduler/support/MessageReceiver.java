package com.lvmama.bms.scheduler.support;

import com.lvmama.bms.core.domain.FailedMessageWrapper;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.core.exception.MsgReceiveException;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.command.MsgSendRequest;
import com.lvmama.bms.core.store.mysql.MsgSerialize4DB;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.monitor.MsgCounter;
import com.lvmama.bms.scheduler.store.MessageStore;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MessagePO;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.exception.DupEntryException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageReceiver {

    private Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    private MessageStore messageStore;

    private MsgTokenStore msgTokenStore;

    private MsgCacheManager msgCacheManager;

    private MsgCounter msgCounter;

    public MessageReceiver(MsgSchedulerAppContext msgSchedulerAppContext) {
        this.messageStore = msgSchedulerAppContext.getMessageStore();
        this.msgTokenStore = msgSchedulerAppContext.getMsgTokenStore();
        this.msgCacheManager = msgSchedulerAppContext.getMsgCacheManager();
        this.msgCounter = msgSchedulerAppContext.getMsgCounter();
    }

    public void receive(MsgSendRequest request) throws MsgReceiveException {

        MsgReceiveContext msgReceiveContext = new MsgReceiveContext();

        for(Message message : request.getMessages()) {

            try {

                MsgCacheManager.CheckResult checkResult = msgCacheManager.checkToken(message.getTokens());
                if(!checkResult.isValid()) {
                    logger.error("tip=find invalid tokens"+checkResult.getInvalidToken()+", validTokenCount="+checkResult.getValidToken().size()+"|"+message);
                    msgReceiveContext.addFailedMessage(message, false,
                            "find invalid tokens"+checkResult.getInvalidToken()+", validTokenCount="+checkResult.getValidToken().size());
                    if(checkResult.getValidToken().size() == 0) {
                        continue;
                    }
                }

                MessagePO messagePO = convertToMsgPO(message);

                if(messagePO.getMsgTypeId()==null) {
                    logger.error("tip=the msgType is not exist|"+message);
                    msgReceiveContext.addFailedMessage(message, false, "find invalid msgType:"+message.getMsgType());
                    continue;
                }

                /**
                 * @update 2018-06-28
                 * 解决阻塞问题
                 */
//                if(message.isReplaceOnExist()) {
//                    Long innerMsgId  = messageStore.updateStatus(messagePO.getMsgId(), MsgStatus.RECEIVE.ordinal(),
//                            MsgStatus.INVALID.ordinal(), messagePO.getMsgTypeId());
//                    if(innerMsgId != null) {
//                        logger.info("tip=replace exist message|innerMsgId={}|{}", innerMsgId, message);
//                        msgTokenStore.deleteByMsgId(innerMsgId, messagePO.getMsgTypeId());
//                    }
//                }


                /**
                 * @upate 2018-06-21
                 * 加入事务解决两个insert中会产生脏数据的问题
                 */
                messageStore.saveMessageAndToken(messagePO, checkResult.getValidToken().toArray(new Integer[0]));

                //统计到达量，即发送TOKEN的数量
                msgCounter.addReceiveCount(messagePO.getId(), messagePO.getMsgTypeId(), message.getTokens());

                logger.info("tip=success save message|innerMsgId={}|{}", messagePO.getId(), message);

            } catch (DupEntryException e) {
                logger.error("tip=receive duplicated message|"+message+"|exception=", e);
            } catch (Exception e) {
                logger.info("tip=fail save message|"+message+"|exception=", e);
                msgReceiveContext.addFailedMessage(message, true, e.getMessage());
            }

        }

        List<FailedMessageWrapper> failedMessages = msgReceiveContext.getFailedMessages();
        if(failedMessages.size() > 0) {
            throw new MsgReceiveException(failedMessages, msgReceiveContext.getErrorMsgs().toString());
        }

    }

    private MessagePO convertToMsgPO(Message message) {

        MessagePO messagePO = new MessagePO();
//        messagePO.setId(message.getInnerId());
        messagePO.setMsgId(message.getMsgId());
        MsgTypePO msgTypePO = msgCacheManager.getMsgType(message.getMsgType());
        if(msgTypePO!=null) {
            messagePO.setMsgTypeId(msgTypePO.getId());
        }
        messagePO.setMsgContent(MsgSerialize4DB.serialize(message.getPayload()));
        messagePO.setMaxRetry(message.getMaxRetry());
        messagePO.setReceiveTime(new Date());
        messagePO.setMsgStatus(MsgStatus.RECEIVE.ordinal());

        return messagePO;

    }

    private class MsgReceiveContext {

        private List<FailedMessageWrapper> failedMessages = new ArrayList<>(0);

        private StringBuilder errorMsgs = new StringBuilder();

        public void addFailedMessage(Message message, boolean isTemporaryRetry, String errorMsg) {
            failedMessages.add(new FailedMessageWrapper(message, isTemporaryRetry));
            errorMsgs.append(errorMsg).append("; ");
        }

        public List<FailedMessageWrapper> getFailedMessages() {
            return failedMessages;
        }

        public String getErrorMsgs() {
            return errorMsgs.toString();
        }
    }

}
