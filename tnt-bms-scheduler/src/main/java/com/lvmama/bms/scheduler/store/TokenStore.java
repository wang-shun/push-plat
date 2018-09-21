package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.TokenPO;

import java.util.List;

public interface TokenStore {

    List<TokenPO> queryAllMsgToken();

}
