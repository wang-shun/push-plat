package com.lvmama.bms.scheduler.store.domain.po;

import java.util.Date;

public class MsgPushFailPO {

    private Long id;

    private String msgId;

    private String msgType;

    private Integer msgTypeId;

    private Integer maxRetry;

    private String msgContent;

    private Date receiveTime;

    private Date pushTime;

    private String token;

    private Integer tokenId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Integer msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String toString() {
        return "MsgPushFailPO{" +
                "id=" + id +
                ", msgId='" + msgId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgTypeId=" + msgTypeId +
                ", maxRetry=" + maxRetry +
                ", msgContent='" + msgContent + '\'' +
                ", receiveTime=" + receiveTime +
                ", pushTime=" + pushTime +
                ", token='" + token + '\'' +
                ", tokenId=" + tokenId +
                '}';
    }
}
