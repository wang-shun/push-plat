package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.statis.TokenStatisPO;

import java.util.Collection;

public interface TokenStatisStore {

    int batchSave(Collection<TokenStatisPO> tokenStatis);

}
