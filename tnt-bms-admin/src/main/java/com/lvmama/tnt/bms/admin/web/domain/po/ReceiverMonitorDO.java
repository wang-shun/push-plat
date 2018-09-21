package com.lvmama.tnt.bms.admin.web.domain.po;


/**
 * @author longhr
 * @version 2017/12/5 0005
 */
public class ReceiverMonitorDO extends BaseDO{
    private static final long serialVersionUID = 3192879665321376573L;
    private String token;
    private String name;
    private Integer reachCount;
    private Integer receiveCount;
    private Integer failCount;
    private Integer sendCount;
    private Long timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
