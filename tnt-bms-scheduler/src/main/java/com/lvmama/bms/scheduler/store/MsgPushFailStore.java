package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.MsgPushFailPO;

import java.util.Date;

public interface MsgPushFailStore {

    int save(MsgPushFailPO msgPushFailPO);

    int deleteExpiredMsg(Date deadline);

}
