package com.lvmama.bms.scheduler.store.domain.po;

import com.lvmama.bms.core.commons.utils.StringUtils;

import java.util.Date;

public class MessagePO {

    private Long id;

    private String msgId;

    private Integer msgTypeId;

    private String msgContent;

    private Integer msgStatus;

    private Date receiveTime;

    private Date modifyTime;

    private String pusherId;

    private Integer maxRetry;

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

    public Integer getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Integer msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPusherId() {
        return pusherId;
    }

    public void setPusherId(String pusherId) {
        this.pusherId = pusherId;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public String toString() {
        return "MessagePO{" +
                "id='" + id + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgTypeId='" + msgTypeId + '\'' +
                ", receiveTime='" + receiveTime + '\'' +
                ", msgContent=" + msgContent +
                '}';
    }
}
