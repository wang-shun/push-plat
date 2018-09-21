package com.lvmama.bms.remoting.lts;

import com.lvmama.bms.nio.NioServer;
import com.lvmama.bms.nio.codec.Encoder;
import com.lvmama.bms.nio.channel.ChannelInitializer;
import com.lvmama.bms.nio.codec.Decoder;
import com.lvmama.bms.nio.config.NioServerConfig;
import com.lvmama.bms.remoting.AbstractRemotingServer;
import com.lvmama.bms.remoting.ChannelEventListener;
import com.lvmama.bms.remoting.RemotingServerConfig;
import com.lvmama.bms.remoting.exception.RemotingException;

import java.net.InetSocketAddress;

/**
 * @author Robert HG (254963746@qq.com) on 2/8/16.
 */
public class LtsRemotingServer extends AbstractRemotingServer {

    private NioServer server;

    public LtsRemotingServer(RemotingServerConfig remotingServerConfig, ChannelEventListener channelEventListener) {
        super(remotingServerConfig, channelEventListener);
    }

    public LtsRemotingServer(RemotingServerConfig remotingServerConfig) {
        this(remotingServerConfig, null);
    }

    @Override
    protected void serverStart() throws RemotingException {
        NioServerConfig serverConfig = new NioServerConfig();
        serverConfig.setBacklog(65536);
        serverConfig.setReuseAddress(true);
        serverConfig.setTcpNoDelay(true);

        serverConfig.setIdleTimeBoth(remotingServerConfig.getServerChannelMaxIdleTimeSeconds());
        serverConfig.setIdleTimeWrite(remotingServerConfig.getWriterIdleTimeSeconds());
        serverConfig.setIdleTimeRead(remotingServerConfig.getReaderIdleTimeSeconds());

        final LtsCodecFactory codecFactory = new LtsCodecFactory(getCodec());

        server = new NioServer(serverConfig, new LtsEventHandler(this, "SERVER"), new ChannelInitializer() {
            @Override
            protected Decoder getDecoder() {
                return codecFactory.getDecoder();
            }

            @Override
            protected Encoder getEncoder() {
                return codecFactory.getEncoder();
            }
        });

        server.bind(new InetSocketAddress(this.remotingServerConfig.getListenPort()));
    }

    @Override
    protected void serverShutdown() throws RemotingException {
        server.shutdownGracefully();
    }

}
