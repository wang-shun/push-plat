package com.lvmama.bms.scheduler.complete.biz;

import com.lvmama.bms.core.protocol.command.MsgPushedRequest;
import com.lvmama.bms.remoting.protocol.RemotingCommand;

/**
 * @author Robert HG (254963746@qq.com) on 11/11/15.
 */
public interface PushBiz {

    /**
     * 如果返回空表示继续执行
     */
    RemotingCommand doBiz(MsgPushedRequest request);

    boolean isAsync();

}
