package com.lvmama.bms.core.protocol.command;

import com.lvmama.bms.core.domain.PushResult;
import com.lvmama.bms.remoting.annotation.NotNull;

import java.util.List;

/**
 * @author Robert HG (254963746@qq.com) on 8/16/14.
 * TaskTracker Job completed request command body
 */
public class MsgPushedRequest extends AbstractRemotingCommandBody {
	private static final long serialVersionUID = 3034213298501228160L;

	private boolean isSpeedFast;
	/**
	 * 是否接受新任务
	 */
	private boolean receiveNewMsg = false;

	@NotNull
	private List<PushResult> pushResults;

	// 是否是重发(重发是批量发)
	private boolean reSend = false;

	public boolean isReceiveNewMsg() {
		return receiveNewMsg;
	}

	public void setReceiveNewMsg(boolean receiveNewMsg) {
		this.receiveNewMsg = receiveNewMsg;
	}

	public List<PushResult> getPushResults() {
		return pushResults;
	}

	public void setPushResults(List<PushResult> pushResults) {
		this.pushResults = pushResults;
	}

	public boolean isReSend() {
		return reSend;
	}

	public void setReSend(boolean reSend) {
		this.reSend = reSend;
	}

	public boolean isSpeedFast() {
		return isSpeedFast;
	}

	public void setSpeedFast(boolean speedFast) {
		isSpeedFast = speedFast;
	}
}
