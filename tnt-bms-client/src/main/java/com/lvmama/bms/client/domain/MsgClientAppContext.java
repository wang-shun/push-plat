package com.lvmama.bms.client.domain;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.remoting.RemotingClientDelegate;

/**
 * @author Robert HG (254963746@qq.com) on 3/30/15.
 */
public class MsgClientAppContext extends AppContext {

    private RemotingClientDelegate remotingClient;

    public RemotingClientDelegate getRemotingClient() {
        return remotingClient;
    }

    public void setRemotingClient(RemotingClientDelegate remotingClient) {
        this.remotingClient = remotingClient;
    }
}

