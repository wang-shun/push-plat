package com.lvmama.bms.pusher.store.mysql.mapper;

import com.lvmama.bms.pusher.store.domain.MsgPusherPO;

import java.util.List;

public interface MsgPusherMapper {

    List<MsgPusherPO> getAllMsgPusher();

    int saveMsgPusher(MsgPusherPO msgPusher);

    MsgPusherPO getMsgPusherById(Integer id);

}
