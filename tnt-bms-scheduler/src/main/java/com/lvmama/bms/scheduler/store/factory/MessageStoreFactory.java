package com.lvmama.bms.scheduler.store.factory;

import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.spi.SPI;
import com.lvmama.bms.scheduler.store.*;

@SPI(key = ExtConfig.MSG_STORE, dftValue = "mysql")
public interface MessageStoreFactory {

    MessageStore getMessageStore();

    MsgTokenStore getMsgTokenStore();

    TokenStore getTokenStore();

    MsgTypeStore getMsgTypeStore();

    GlobalStatisStore getGlobalStatisStore();

    TokenStatisStore getTokenStatisStore();

    MsgTypeStatisStore getMsgTypeStatisStore();

    MsgTokenStatisStore getMsgTokenStatisStore();

    MsgPushFailStore getMsgPushFailStore();

}
