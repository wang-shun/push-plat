package com.lvmama.bms.client.domain;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.lvmama.bms.core.domain.FailedMessageWrapper;
import com.lvmama.bms.core.domain.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert HG (254963746@qq.com) on 8/13/14.
 * 返回给客户端的
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 1466250084442936848L;
	private boolean success;
    private String msg;
    private String code;

    // 如果success 为false, 这个才会有值
    public List<Message> getFailedMessages() {

        List<Message> failedMessages = null;

        if(failedMessageWrappers.size() > 0) {
            failedMessages = new ArrayList<>();
            for(FailedMessageWrapper failedMessageWrapper : failedMessageWrappers) {
                failedMessages.add(failedMessageWrapper.getFailedMessage());
            }
        }

        return failedMessages;

    }

    private List<FailedMessageWrapper> failedMessageWrappers = new ArrayList<>(0);

    @JSONField(serialize = false)
    public List<FailedMessageWrapper> getFailedMessageWrappers() {
        return failedMessageWrappers;
    }

    public void setFailedMessageWrappers(List<FailedMessageWrapper> failedMessageWrappers) {
        this.failedMessageWrappers = failedMessageWrappers;
    }

    public void setFailedMessageWrappers(List<Message> failedMessages, boolean isTemporaryRetry) {
        for(Message message : failedMessages) {
            addFailedMessageWrapper(message, isTemporaryRetry);
        }
    }

    public void addFailedMessageWrapper(List<FailedMessageWrapper> failedMessageWrapper){
        this.failedMessageWrappers.addAll(failedMessageWrapper);
    }

    public void addFailedMessageWrapper(Message failedMessage, boolean isTemporaryRetry){
        this.failedMessageWrappers.add(new FailedMessageWrapper(failedMessage, isTemporaryRetry));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
