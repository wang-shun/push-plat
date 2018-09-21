package com.lvmama.bms.client.support;

import com.lvmama.bms.remoting.protocol.RemotingCommand;

/**
 * @author Robert HG (254963746@qq.com) on 5/30/15.
 */
public interface SendCallback {

    public void call(final RemotingCommand responseCommand);

}
