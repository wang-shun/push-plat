package com.lvmama.tnt.bms.admin.web.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author longhr
 * @version 2017/11/10 0010
 */
public class NewsSpeedDetailVO implements Serializable{
    private static final long serialVersionUID = -3297070985638157879L;

    private String msgId;
//    useless
    private String msgType;
    private Integer status;
    private String token;
    private String name;
    private String pushType;
    private String pushUrl;
    private Date receiveTime;
    private Date pushTime;

    private String businessMsgID;

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }
}
