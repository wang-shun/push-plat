package com.lvmama.bms.alarm;

/**
 * 要保证同一条消息不会被重复发送多次
 * @author Robert HG (254963746@qq.com)  on 2/17/16.
 */
public abstract class AbstractAlarmNotifier<T extends AlarmMessage> implements AlarmNotifier<T> {

    protected boolean asyncSend;

    @Override
    public final void notice(T message) {
        if (asyncSend) {
            asyncNotice(message);
        } else {
            doNotice(message);
        }
    }

    protected abstract void doNotice(T message);

    protected abstract void asyncNotice(T message);

    public boolean isAsyncSend() {
        return asyncSend;
    }

    public void setAsyncSend(boolean asyncSend) {
        this.asyncSend = asyncSend;
    }
}
