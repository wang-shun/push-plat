package com.lvmama.bms.pusher.processor;

import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.remoting.RemotingProcessor;

/**
 * @author Robert HG (254963746@qq.com) on 8/16/14.
 */
public abstract class AbstractProcessor implements RemotingProcessor {

    protected MsgPusherAppContext appContext;

    protected AbstractProcessor(MsgPusherAppContext appContext) {
        this.appContext = appContext;
    }
}
