package com.lvmama.bms.remoting;

/**
 * @author Robert HG (254963746@qq.com) on 11/4/15.
 */
public interface ChannelFuture {

    boolean isConnected();

    Channel getChannel();

    boolean awaitUninterruptibly(long timeoutMillis);

    boolean isDone();

    Throwable cause();
}
