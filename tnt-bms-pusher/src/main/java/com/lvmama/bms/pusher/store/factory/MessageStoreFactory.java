package com.lvmama.bms.pusher.store.factory;

import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.spi.SPI;
import com.lvmama.bms.pusher.store.ConvertMapStore;
import com.lvmama.bms.pusher.store.MsgPusherStore;

@SPI(key = ExtConfig.MSG_STORE, dftValue = "mysql")
public interface MessageStoreFactory {

    MsgPusherStore getMsgPushStore();

    ConvertMapStore getConvertMapperStore();

}
