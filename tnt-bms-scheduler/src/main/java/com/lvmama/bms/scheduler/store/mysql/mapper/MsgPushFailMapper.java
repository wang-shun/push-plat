package com.lvmama.bms.scheduler.store.mysql.mapper;

import com.lvmama.bms.scheduler.store.domain.po.MsgPushFailPO;

import java.util.Date;

public interface MsgPushFailMapper {

    int save(MsgPushFailPO msgPushFailPO);

    int deleteExpiredMsg(Date deadline);

}
