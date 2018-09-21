package com.lvmama.bms.core.domain;

import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.exception.MsgSendException;

import java.util.Arrays;

public class Message {

    private String msgId;

    private String msgType;

    private Object payload;

    private String[] tokens;

    private Integer maxRetry;

    private boolean replaceOnExist;

    public Message() {
    }

    public Message(String msgId, String msgType, Object payload, String... tokens) {
        this.msgId = msgId;
        this.msgType = msgType;
        this.payload = payload;
        this.tokens = tokens;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public boolean isReplaceOnExist() {
        return replaceOnExist;
    }

    public void setReplaceOnExist(boolean replaceOnExist) {
        this.replaceOnExist = replaceOnExist;
    }

    private String innerId;

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public String getInnerId() {
        return innerId;
    }

    public void checkField() throws MsgSendException {

        if(StringUtils.isEmpty(msgId)) {
            throw new MsgSendException("The msgId can not empty");
        }

        if(StringUtils.isEmpty(msgType)) {
            throw new MsgSendException("The msgType can not empty");
        }

        if(payload==null) {
            throw new MsgSendException("The payload can not null");
        }

        if(tokens==null || tokens.length==0) {
            throw new MsgSendException("The tokens size must > 0");
        }


    }

    @Override
    public String toString() {
        return  "msgId=" + msgId +
                "|msgType=" + msgType +
                "|tokens=" + Arrays.toString(tokens) +
                "|maxRetry=" + maxRetry +
                "|replaceOnExist=" + replaceOnExist +
                "|payload=" + payload;
    }

}
