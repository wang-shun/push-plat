package com.lvmama.bms.remoting.lts;

import com.lvmama.bms.nio.NioClient;
import com.lvmama.bms.nio.codec.Decoder;
import com.lvmama.bms.nio.codec.Encoder;
import com.lvmama.bms.nio.config.NioClientConfig;
import com.lvmama.bms.nio.handler.Futures;
import com.lvmama.bms.remoting.AbstractRemotingClient;
import com.lvmama.bms.remoting.ChannelEventListener;
import com.lvmama.bms.remoting.exception.RemotingException;
import com.lvmama.bms.nio.channel.ChannelInitializer;
import com.lvmama.bms.remoting.RemotingClientConfig;
import com.lvmama.bms.remoting.ChannelFuture;

import java.net.SocketAddress;

/**
 * @author Robert HG (254963746@qq.com) on 2/8/16.
 */
public class LtsRemotingClient extends AbstractRemotingClient {

    private NioClient client;

    public LtsRemotingClient(RemotingClientConfig remotingClientConfig, ChannelEventListener channelEventListener) {
        super(remotingClientConfig, channelEventListener);
    }

    public LtsRemotingClient(RemotingClientConfig remotingClientConfig) {
        this(remotingClientConfig, null);
    }

    @Override
    protected void clientStart() throws RemotingException {
        NioClientConfig clientConfig = new NioClientConfig();
        clientConfig.setTcpNoDelay(true);
        clientConfig.setIdleTimeBoth(remotingClientConfig.getClientChannelMaxIdleTimeSeconds());
        clientConfig.setIdleTimeRead(remotingClientConfig.getReaderIdleTimeSeconds());
        clientConfig.setIdleTimeWrite(remotingClientConfig.getWriterIdleTimeSeconds());

        final LtsCodecFactory codecFactory = new LtsCodecFactory(getCodec());

        this.client = new NioClient(clientConfig, new LtsEventHandler(this, "CLIENT"), new ChannelInitializer() {
            @Override
            protected Decoder getDecoder() {
                return codecFactory.getDecoder();
            }

            @Override
            protected Encoder getEncoder() {
                return codecFactory.getEncoder();
            }
        });
    }

    @Override
    protected void clientShutdown() {
        client.shutdownGracefully();
    }

    @Override
    protected ChannelFuture connect(SocketAddress socketAddress) {
        Futures.ConnectFuture connectFuture = client.connect(socketAddress);
        return new LtsChannelFuture(connectFuture);
    }

}
