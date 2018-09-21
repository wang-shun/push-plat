package com.lvmama.bms.core.exception;

/**
 * @author Robert HG (254963746@qq.com) on 5/12/15.
 */
public class MsgSendException extends RuntimeException {

	private static final long serialVersionUID = 8375498515729588730L;

	public MsgSendException() {
        super();
    }

    public MsgSendException(String message) {
        super(message);
    }

    public MsgSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MsgSendException(Throwable cause) {
        super(cause);
    }

}
