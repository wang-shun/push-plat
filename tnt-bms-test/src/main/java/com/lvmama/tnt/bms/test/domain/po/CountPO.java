package com.lvmama.tnt.bms.test.domain.po;

import java.util.Date;

/**
 *
 */
public class CountPO {
    private Integer sendCount;
    private Date createTime;

    public CountPO(){}

    public CountPO(Integer sendCount, Date createTime) {
        this.sendCount = sendCount;
        this.createTime = createTime;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
