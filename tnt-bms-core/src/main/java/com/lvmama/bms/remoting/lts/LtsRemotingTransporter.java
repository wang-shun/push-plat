package com.lvmama.bms.remoting.lts;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.remoting.*;

/**
 * @author Robert HG (254963746@qq.com) on 2/8/16.
 */
public class LtsRemotingTransporter implements RemotingTransporter {
    @Override
    public RemotingServer getRemotingServer(AppContext appContext, RemotingServerConfig remotingServerConfig) {
        return new LtsRemotingServer(remotingServerConfig);
    }

    @Override
    public RemotingClient getRemotingClient(AppContext appContext, RemotingClientConfig remotingClientConfig) {
        return new LtsRemotingClient(remotingClientConfig);
    }
}
