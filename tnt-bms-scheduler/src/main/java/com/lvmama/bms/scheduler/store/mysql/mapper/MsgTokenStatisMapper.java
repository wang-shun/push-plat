package com.lvmama.bms.scheduler.store.mysql.mapper;

import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTokenStatisPO;

public interface MsgTokenStatisMapper {

    int saveOrUpdate(MsgTokenStatisPO msgTokenStatis);

}
