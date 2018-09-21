package com.lvmama.bms.pusher.store;

import com.lvmama.bms.pusher.store.domain.MsgPusherPO;

import java.util.List;

public interface MsgPusherStore {

    List<MsgPusherPO> getAllMsgPusher();

    int saveMsgPusher(MsgPusherPO msgPusher);

    MsgPusherPO getMsgPusherById(Integer id);

}
