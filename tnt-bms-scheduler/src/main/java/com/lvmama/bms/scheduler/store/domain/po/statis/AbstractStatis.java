package com.lvmama.bms.scheduler.store.domain.po.statis;

public class AbstractStatis {

    private int receiveCount;

    private int sendCount;

    private int reachCount;

    private int failCount;

    private Long timestamp;

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getReachCount() {
        return reachCount;
    }

    public void setReachCount(Integer reachCount) {
        this.reachCount = reachCount;
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
