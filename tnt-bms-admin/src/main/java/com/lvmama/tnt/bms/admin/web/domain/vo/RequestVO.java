package com.lvmama.tnt.bms.admin.web.domain.vo;

import java.io.Serializable;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
public class RequestVO implements Serializable{

    private static final long serialVersionUID = 1166208023783234826L;
    private String msgType;
    private String startTime;
    private String endTime;
    private String msgId;
    private String token;
    private String pageNo;


    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "RequestVO{" +
                "msgType='" + msgType + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", msgId='" + msgId + '\'' +
                ", token='" + token + '\'' +
                ", pageNo='" + pageNo + '\'' +
                '}';
    }
}
