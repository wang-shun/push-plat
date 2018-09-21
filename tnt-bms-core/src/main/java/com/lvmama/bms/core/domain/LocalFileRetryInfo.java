package com.lvmama.bms.core.domain;

/**
 *
 */
public class LocalFileRetryInfo {
    private Long createTime;
    private Integer retryCount;

    public LocalFileRetryInfo(Long createTime, Integer retryCount) {
        this.createTime = createTime;
        this.retryCount = retryCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "LocalFileRetryInfo{" +
                "createTime=" + createTime +
                ", retryCount=" + retryCount +
                '}';
    }
}