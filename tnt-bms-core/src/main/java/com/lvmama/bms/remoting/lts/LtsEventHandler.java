package com.lvmama.bms.remoting.lts;

import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.nio.channel.NioChannel;
import com.lvmama.bms.nio.handler.NioHandler;
import com.lvmama.bms.remoting.AbstractRemoting;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.RemotingEvent;
import com.lvmama.bms.remoting.RemotingEventType;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.nio.idle.IdleState;
import com.lvmama.bms.remoting.common.RemotingHelper;
import com.lvmama.bms.remoting.protocol.RemotingCommand;

import static com.lvmama.bms.nio.idle.IdleState.BOTH_IDLE;

/**
 * @author Robert HG (254963746@qq.com) on 2/8/16.
 */
public class LtsEventHandler implements NioHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LtsEventHandler.class);

    private AbstractRemoting remoting;
    private String sideType;      // SERVER , CLIENT

    public LtsEventHandler(AbstractRemoting remoting, String sideType) {
        this.remoting = remoting;
        this.sideType = sideType;
    }

    @Override
    public void exceptionCaught(NioChannel channel, Exception cause) {
        Channel ch = new LtsChannel(channel);
        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ch);
        LOGGER.warn(sideType + ": exceptionCaught {}", remoteAddress, cause);

        if (remoting.getChannelEventListener() != null) {
            remoting.putRemotingEvent(new RemotingEvent(RemotingEventType.EXCEPTION, remoteAddress, ch));
        }
        RemotingHelper.closeChannel(ch);
    }

    @Override
    public void messageReceived(NioChannel channel, Object msg) throws Exception {
        if (msg != null && msg instanceof RemotingCommand) {
            remoting.processMessageReceived(new LtsChannel(channel), (RemotingCommand) msg);
        }
    }

    @Override
    public void channelConnected(NioChannel channel) {
        Channel ch = new LtsChannel(channel);
        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ch);
        LOGGER.info("{}: channelConnected, the channel[{}]", sideType, remoteAddress);

        if (remoting.getChannelEventListener() != null) {
            remoting.putRemotingEvent(new RemotingEvent(RemotingEventType.CONNECT, remoteAddress, ch));
        }
    }

    @Override
    public void channelIdle(NioChannel channel, IdleState state) {
        if (state == null) {
            return;
        }
        Channel ch = new LtsChannel(channel);

        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ch);

        if (BOTH_IDLE == state) {
            LOGGER.info("{}: IDLE [{}]", sideType, remoteAddress);
            RemotingHelper.closeChannel(ch);
        }

        if (remoting.getChannelEventListener() != null) {
            RemotingEventType remotingEventType = null;
            switch (state) {
                case BOTH_IDLE:
                    remotingEventType = RemotingEventType.ALL_IDLE;
                    break;
                case READER_IDLE:
                    remotingEventType = RemotingEventType.READER_IDLE;
                    break;
                case WRITER_IDLE:
                    remotingEventType = RemotingEventType.WRITER_IDLE;
                    break;
            }
            remoting.putRemotingEvent(new RemotingEvent(remotingEventType, remoteAddress, ch));
        }
    }
}
