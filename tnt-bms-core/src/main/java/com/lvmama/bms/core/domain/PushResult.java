package com.lvmama.bms.core.domain;

import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.json.JSON;

import java.io.Serializable;

/**
 * @author Robert HG (254963746@qq.com) on 8/19/14.
 * TaskTracker 任务执行结果
 */
public class PushResult implements Serializable{

	private static final long serialVersionUID = 8622758290605000897L;

	private MessageDTO message;

    private Action action;

    private String msg;

    private Long timeCost;

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }

    @Override
    public String toString() {
        return  "action=" + action +
                "|msg=" + msg +
                "|timeCost=" + timeCost + "|" +
                message;
    }
}
