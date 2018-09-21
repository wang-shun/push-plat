package com.lvmama.bms.scheduler.store.domain.po.statis;

public class MsgTokenStatisPO extends AbstractStatis{

    private String msgId;

    private Integer msgTypeId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Integer msgTypeId) {
        this.msgTypeId = msgTypeId;
    }
}
