package com.lvmama.bms.scheduler.store.domain.po.statis;

public class GlobalStatisPO extends AbstractStatis {

    public boolean checkData() {
        return getReachCount()+getFailCount()+getReceiveCount()+getSendCount()>0;
    }

}
