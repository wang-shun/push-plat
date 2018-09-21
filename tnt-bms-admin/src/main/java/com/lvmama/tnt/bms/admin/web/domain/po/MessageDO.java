package com.lvmama.tnt.bms.admin.web.domain.po;

import java.util.Date;

public class MessageDO {
    private Long id;

    private String msgId;

    private Integer msgTypeId;

    private String msgContent;

    private Integer msgStatus;

    private Date receiveTime;

    private Date modifyTime;

    private String pusherId;

    private Integer maxRetry;

    private Integer nowRetry;

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

    public Integer getNowRetry() {
        return nowRetry;
    }

    public void setNowRetry(Integer nowRetry) {
        this.nowRetry = nowRetry;
    }
}