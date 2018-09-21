package com.lvmama.bms.pusher.protocol;

import com.lvmama.bms.core.domain.enums.ProtocolType;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.protocol.http.HttpPusher;
import com.lvmama.bms.pusher.protocol.rpc.RpcPusherProxy;

import java.util.HashMap;
import java.util.Map;

public class ProtocolPusherFactory {

    private Map<Integer, ProtocolPusher> protocolPusherMap = new HashMap<Integer, ProtocolPusher>();

    public ProtocolPusherFactory(MsgPusherAppContext appContext) {
        protocolPusherMap.put(ProtocolType.Http.getValue(), new HttpPusher(appContext));
        protocolPusherMap.put(ProtocolType.RPC.getValue(), new RpcPusherProxy(appContext));
    }

    public ProtocolPusher getProtocolPusher(Integer type) {
       return protocolPusherMap.get(type);
    }

}
