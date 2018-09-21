package com.lvmama.bms.core.protocol.command;

/**
 * Job pull request
 * Robert HG (254963746@qq.com) on 3/25/15.
 */
public class MsgPullRequest extends AbstractRemotingCommandBody {

	private static final long serialVersionUID = 9222159289387747395L;

	private Integer slowIdleThreads;

	private Integer fastIdleThreads;

    public Integer getSlowIdleThreads() {
        return slowIdleThreads;
    }

    public void setSlowIdleThreads(Integer slowIdleThreads) {
        this.slowIdleThreads = slowIdleThreads;
    }

    public Integer getFastIdleThreads() {
        return fastIdleThreads;
    }

    public void setFastIdleThreads(Integer fastIdleThreads) {
        this.fastIdleThreads = fastIdleThreads;
    }
}
