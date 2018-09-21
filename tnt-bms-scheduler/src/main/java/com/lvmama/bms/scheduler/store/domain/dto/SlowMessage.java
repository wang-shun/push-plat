package com.lvmama.bms.scheduler.store.domain.dto;

public class SlowMessage {

    public SlowMessage() {
    }

    public SlowMessage(Integer msgTypeId, Integer tokenId) {
        this.msgTypeId = msgTypeId;
        this.tokenId = tokenId;
    }

    private Integer msgTypeId;

    private Integer tokenId;

    public Integer getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(Integer msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }
}
