package com.lvmama.bms.nio.handler;

import com.lvmama.bms.nio.channel.NioChannel;
import com.lvmama.bms.nio.channel.NioChannelImpl;

/**
 * @author Robert HG (254963746@qq.com) on 2/4/16.
 */
public class Futures {

    public static CloseFuture newCloseFuture() {
        return new CloseFuture();
    }

    public static WriteFuture newWriteFuture() {
        return new WriteFuture();
    }

    public static ConnectFuture newConnectFuture() {
        return new ConnectFuture();
    }

    public static class ConnectFuture extends IoFuture {

        private NioChannelImpl channel;

        public void setChannel(NioChannelImpl channel) {
            this.channel = channel;
        }

        public NioChannel channel() {
            return channel;
        }
    }

    public static class CloseFuture extends IoFuture {

        private NioChannelImpl channel;

        public void setChannel(NioChannelImpl channel) {
            this.channel = channel;
        }

        public NioChannel channel() {
            return channel;
        }
    }

    public static class WriteFuture extends IoFuture {
    }
}
