package com.lvmama.bms.remoting.lts;

import com.lvmama.bms.nio.handler.IoFuture;
import com.lvmama.bms.nio.handler.IoFutureListener;
import com.lvmama.bms.remoting.ChannelHandlerListener;
import com.lvmama.bms.remoting.Future;
import com.lvmama.bms.remoting.ChannelHandler;

/**
 * @author Robert HG (254963746@qq.com) on 2/8/16.
 */
public class LtsChannelHandler implements ChannelHandler {

    private IoFuture future;

    public LtsChannelHandler(IoFuture future) {
        this.future = future;
    }

    @Override
    public ChannelHandler addListener(final ChannelHandlerListener listener) {
        future.addListener(new IoFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                listener.operationComplete(future);
            }
        });
        return this;
    }
}
