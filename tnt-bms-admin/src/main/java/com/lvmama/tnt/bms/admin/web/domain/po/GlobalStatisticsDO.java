package com.lvmama.tnt.bms.admin.web.domain.po;

/**
 * @author longhr
 * @version 2017/11/7 0007
 */
public class GlobalStatisticsDO extends BaseDO {
    private static final long serialVersionUID = -1464202636187586259L;

    private Integer reachCount;
    private Integer receiveCount;
    private Integer failCount;
    private Integer sendCount;
    private Long timestamp;

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
