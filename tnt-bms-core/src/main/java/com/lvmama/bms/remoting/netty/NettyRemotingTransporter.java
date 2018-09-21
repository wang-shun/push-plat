package com.lvmama.bms.remoting.netty;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.remoting.*;

/**
 * @author Robert HG (254963746@qq.com) on 11/6/15.
 */
public class NettyRemotingTransporter implements RemotingTransporter {

    @Override
    public RemotingServer getRemotingServer(AppContext appContext, RemotingServerConfig remotingServerConfig) {
        return new NettyRemotingServer(appContext, remotingServerConfig);
    }

    @Override
    public RemotingClient getRemotingClient(AppContext appContext, RemotingClientConfig remotingClientConfig) {
        return new NettyRemotingClient(appContext, remotingClientConfig);
    }
}
