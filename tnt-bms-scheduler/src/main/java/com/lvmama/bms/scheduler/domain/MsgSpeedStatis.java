package com.lvmama.bms.scheduler.domain;

import java.io.Serializable;

public class MsgSpeedStatis implements Serializable {

    private Long startTime;

    private Integer slowCount;

    private Integer totalCount;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getSlowCount() {
        return slowCount;
    }

    public void setSlowCount(Integer slowCount) {
        this.slowCount = slowCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer addTotalCount() {
        return ++totalCount;
    }

    public Integer addSlowCount() {
        return ++slowCount;
    }
}
