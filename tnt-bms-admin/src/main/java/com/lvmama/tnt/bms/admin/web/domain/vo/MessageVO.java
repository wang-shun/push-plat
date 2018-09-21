package com.lvmama.tnt.bms.admin.web.domain.vo;

import java.io.Serializable;

/**
 *
 */
public class MessageVO implements Serializable {
    private static final long serialVersionUID = -8253979026354449462L;

    private String msgId;

    private String msgType;

    private Object payload;

    private String tokens;

    private String maxRetry;

    private String replaceOnExist;

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

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(String maxRetry) {
        this.maxRetry = maxRetry;
    }

    public String getReplaceOnExist() {
        return replaceOnExist;
    }

    public void setReplaceOnExist(String replaceOnExist) {
        this.replaceOnExist = replaceOnExist;
    }
}
