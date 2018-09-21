package com.lvmama.tnt.bms.admin.web.domain.po;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
public class NewsTypeStatisticsDO extends BaseDO {
    private static final long serialVersionUID = 381025736143684456L;

    private String msgType;
    private Integer reachCount;
    private Integer receiveCount;
    private Integer failCount;
    private Integer sendCount;
    private Long timestamp;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getReachCount() {
        return reachCount;
    }

    public void setReachCount(Integer reachCount) {
        this.reachCount = reachCount;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
