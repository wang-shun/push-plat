package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;

import java.util.List;

public interface MsgTypeStore {

    List<MsgTypePO> queryAllMsgType();

}
