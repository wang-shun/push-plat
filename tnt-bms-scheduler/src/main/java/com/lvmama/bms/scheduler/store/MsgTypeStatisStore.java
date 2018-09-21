package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTypeStatisPO;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface MsgTypeStatisStore {

    int batchSave(Collection<MsgTypeStatisPO> msgTypeStatis);

    List<MsgTypeStatisPO> sum(Date after, Date before);

}
