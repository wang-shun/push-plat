package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTokenStatisPO;

public interface MsgTokenStatisStore {

    int saveOrUpdate(MsgTokenStatisPO msgTokenStatis);

}
