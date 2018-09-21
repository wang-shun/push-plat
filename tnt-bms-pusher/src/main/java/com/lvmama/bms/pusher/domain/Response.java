package com.lvmama.bms.pusher.domain;

import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.dto.MessageDTO;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 */
public class Response {

    private Action action;

    private String msg;

    private MessageDTO message;

    /**
     * 是否接收新任务
     */
    private boolean receiveNewJob = true;

    private Long timeCost;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public boolean isReceiveNewJob() {
        return receiveNewJob;
    }

    public void setReceiveNewJob(boolean receiveNewJob) {
        this.receiveNewJob = receiveNewJob;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }
}
