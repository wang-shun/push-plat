package com.lvmama.bms.core.protocol.command;


import com.lvmama.bms.core.domain.FailedMessageWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert HG (254963746@qq.com) on 7/24/14.
 * 任务传递信息
 */
public class MsgSendResponse extends AbstractRemotingCommandBody {

	private static final long serialVersionUID = 9133108871954698698L;

	private Boolean success = true;

    private String msg;

    private List<FailedMessageWrapper> failedMessages = new ArrayList<>(0);

    public List<FailedMessageWrapper> getFailedMessages() {
        return failedMessages;
    }

    public void setFailedMessages(List<FailedMessageWrapper> failedMessages) {
        this.failedMessages = failedMessages;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
