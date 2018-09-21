package com.lvmama.tnt.bms.admin.web.domain.po;

import java.util.Date;

/**
 * @author longhr
 * @version 2017/11/7 0007
 */
public class NewsSendSpeedDO extends BaseDO{

    private static final long serialVersionUID = 6301002006003592442L;

    //msg表主键ID
    private String msgId;
    private String msgTypeId;
    private Integer reachCount;
    private Integer sendCount;
    private Integer failCount;
    private Long timestamp;

    private String businessMsgID;
    private String msgType;
    private Date receiveTime;

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getBusinessMsgID() {
        return businessMsgID;
    }

    public void setBusinessMsgID(String businessMsgID) {
        this.businessMsgID = businessMsgID;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(String msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public Integer getReachCount() {
        return reachCount;
    }

    public void setReachCount(Integer reachCount) {
        this.reachCount = reachCount;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
