package com.lvmama.bms.core.exception;

import com.lvmama.bms.core.domain.FailedMessageWrapper;
import java.util.List;


public class MsgReceiveException extends Exception {

    private List<FailedMessageWrapper> failedMessages;

    public MsgReceiveException() {
    }

    public MsgReceiveException(List<FailedMessageWrapper> failedMessages, String errorMsg) {
        super(errorMsg);
        this.failedMessages = failedMessages;
    }

    public List<FailedMessageWrapper> getFailedMessages() {
        return failedMessages;
    }

}
