package com.lvmama.bms.remoting.common;

import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.ChannelHandlerListener;
import com.lvmama.bms.remoting.Future;

import java.net.InetSocketAddress;
import java.net.SocketAddress;


/**
 * 通信层一些辅助方法
 */
public class RemotingHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemotingHelper.class);

    /**
     * IP:PORT
     */
    public static SocketAddress string2SocketAddress(final String addr) {
        String[] s = addr.split(":");
        return new InetSocketAddress(s[0], Integer.valueOf(s[1]));
    }

    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        final SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }

            return addr;
        }

        return "";
    }

    public static void closeChannel(Channel channel) {
        final String addrRemote = RemotingHelper.parseChannelRemoteAddr(channel);
        channel.close().addListener(new ChannelHandlerListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                LOGGER.info("closeChannel: close the connection to remote address[{}] result: {}", addrRemote,
                        future.isSuccess());
            }
        });
    }

}
