package com.lvmama.bms.core.domain;

public class FailedMessageWrapper {

    private Message failedMessage;

    /**
     * 是否本地暂存，稍后重试
     */
    private boolean isTemporaryRetry;

    public FailedMessageWrapper() {
    }

    public FailedMessageWrapper(Message message, boolean isTemporaryRetry) {
        this.failedMessage = message;
        this.isTemporaryRetry = isTemporaryRetry;
    }

    public Message getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(Message failedMessage) {
        this.failedMessage = failedMessage;
    }

    public boolean isTemporaryRetry() {
        return isTemporaryRetry;
    }

    public void setTemporaryRetry(boolean temporaryRetry) {
        isTemporaryRetry = temporaryRetry;
    }

}
