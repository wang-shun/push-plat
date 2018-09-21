package com.lvmama.bms.extend.rpc;

public class Message {

    private String id;

    private String msgId;

    private String msgType;

    private Object msgContent;

    private String pushUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Object getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(Object msgContent) {
        this.msgContent = msgContent;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgContent=" + msgContent +
                ", pushUrl='" + pushUrl + '\'' +
                '}';
    }
}
