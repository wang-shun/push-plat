package com.lvmama.bms.scheduler.store.mysql.mapper;

import com.lvmama.bms.scheduler.store.domain.po.TokenPO;

import java.util.List;

public interface TokenMapper {

    List<TokenPO> queryAllMsgToken();

}
