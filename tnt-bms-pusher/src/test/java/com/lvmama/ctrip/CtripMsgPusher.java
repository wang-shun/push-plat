package com.lvmama.ctrip;

import com.lvmama.bms.extend.rpc.Message;
import com.lvmama.bms.extend.rpc.Result;
import com.lvmama.bms.extend.rpc.RpcPusher;

public class CtripMsgPusher implements RpcPusher {

    @Override
    public Result push(Message message) {
        System.out.println(CtripConstant.class);
        return Result.SUCCESS;
    }
}
