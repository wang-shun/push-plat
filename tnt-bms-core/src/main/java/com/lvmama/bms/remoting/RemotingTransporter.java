package com.lvmama.bms.remoting;

import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.spi.SPI;

/**
 * @author Robert HG (254963746@qq.com) on 11/6/15.
 */
@SPI(key = ExtConfig.REMOTING, dftValue = "netty")
public interface RemotingTransporter {

    RemotingServer getRemotingServer(AppContext appContext, RemotingServerConfig remotingServerConfig);

    RemotingClient getRemotingClient(AppContext appContext, RemotingClientConfig remotingClientConfig);

}
