package com.lvmama.bms.remoting.mina;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.remoting.*;

/**
 * @author Robert HG (254963746@qq.com) on 11/6/15.
 */
public class MinaRemotingTransporter implements RemotingTransporter {
    @Override
    public RemotingServer getRemotingServer(AppContext appContext, RemotingServerConfig remotingServerConfig) {
        return new MinaRemotingServer(remotingServerConfig);
    }

    @Override
    public RemotingClient getRemotingClient(AppContext appContext, RemotingClientConfig remotingClientConfig) {
        return new MinaRemotingClient(remotingClientConfig);
    }
}
