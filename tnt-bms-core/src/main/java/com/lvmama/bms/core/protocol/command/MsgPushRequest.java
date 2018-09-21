package com.lvmama.bms.core.protocol.command;


import com.lvmama.bms.core.domain.dto.MessageDTO;

public class MsgPushRequest extends AbstractRemotingCommandBody {

    private MessageDTO message;

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }
}
