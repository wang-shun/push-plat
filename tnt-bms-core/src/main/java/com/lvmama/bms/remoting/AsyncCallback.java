package com.lvmama.bms.remoting;


/**
 * 异步调用应答回调接口
 */
public interface AsyncCallback {

    public void operationComplete(final ResponseFuture responseFuture);
}
