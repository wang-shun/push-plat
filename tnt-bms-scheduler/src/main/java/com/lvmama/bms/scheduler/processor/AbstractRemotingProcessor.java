package com.lvmama.bms.scheduler.processor;

import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.remoting.RemotingProcessor;

/**
 * @author Robert HG (254963746@qq.com) on 8/16/14.
 */
public abstract class AbstractRemotingProcessor implements RemotingProcessor {

    protected MsgSchedulerAppContext appContext;

    public AbstractRemotingProcessor(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

}
