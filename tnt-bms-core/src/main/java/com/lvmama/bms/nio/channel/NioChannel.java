package com.lvmama.bms.nio.channel;

import com.lvmama.bms.nio.codec.Encoder;
import com.lvmama.bms.nio.handler.Futures;
import com.lvmama.bms.nio.codec.Decoder;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author Robert HG (254963746@qq.com) on 1/24/16.
 */
public interface NioChannel {

    long getId();

    SocketAddress remoteAddress();

    SocketAddress localAddress();

    Futures.WriteFuture writeAndFlush(Object msg);

    Futures.CloseFuture close();

    Futures.CloseFuture getCloseFuture();

    boolean isConnected();

    boolean isOpen();

    boolean isClosed();

    SocketChannel socketChannel();

    void setLastReadTime(long lastReadTime);

    void setLastWriteTime(long lastWriteTime);

    Decoder getDecoder();

    Encoder getEncoder();
}
