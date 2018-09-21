package com.lvmama.bms.core.failstore;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.spi.SPI;

/**
 * Robert HG (254963746@qq.com) on 5/21/15.
 */
@SPI(key = ExtConfig.FAIL_STORE, dftValue = "leveldb")
public interface FailStoreFactory {

    public FailStore getFailStore(Config config, String storePath);

}
