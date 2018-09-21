package com.lvmama.bms.core.protocol.command;

import com.lvmama.bms.core.domain.Message;

import java.util.Date;
import java.util.List;

public class MsgSendRequest extends AbstractRemotingCommandBody {

    private List<Message> messages;

    private Date sendTime;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}