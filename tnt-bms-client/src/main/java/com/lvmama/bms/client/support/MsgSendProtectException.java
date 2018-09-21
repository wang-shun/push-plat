package com.lvmama.bms.client.support;

import com.lvmama.bms.core.exception.MsgSendException;

/**
 * @author Robert HG (254963746@qq.com) on 5/21/15.
 */
public class MsgSendProtectException extends MsgSendException {

	private static final long serialVersionUID = -5502779460920973581L;
	int concurrentSize;

    public MsgSendProtectException(int concurrentSize) {
        super();
        this.concurrentSize = concurrentSize;
    }

    public MsgSendProtectException(int concurrentSize, String message) {
        super(message);
        this.concurrentSize = concurrentSize;
    }

    public MsgSendProtectException(int concurrentSize, String message, Throwable cause) {
        super(message, cause);
        this.concurrentSize = concurrentSize;
    }

    public MsgSendProtectException(int concurrentSize, Throwable cause) {
        super(cause);
        this.concurrentSize = concurrentSize;
    }
}
