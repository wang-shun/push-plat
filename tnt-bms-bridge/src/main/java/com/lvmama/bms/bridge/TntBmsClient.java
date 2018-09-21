package com.lvmama.bms.bridge;

import com.lvmama.bms.bridge.callback.PayloadCallback;
import com.lvmama.bms.bridge.callback.TokenCallback;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.domain.Message;

import java.util.List;

public class TntBmsClient {

    public static TntBmsClient getInstance() {
        return new TntBmsClient();
    }

    public Response send() {
        Message message = build();
        return MessageClientFactory.getInstance().sendMessage(message);
    }

    private String msgId;

    private String msgType;

    private Object payload;

    private List<String> tokens;

    private Integer maxRetry;

    private boolean replaceOnExist;

    private PayloadCallback payloadCallback;

    private TokenCallback tokenCallback;

    public TntBmsClient msgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public TntBmsClient msgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public TntBmsClient payload(Object payload) {
        this.payload = payload;
        return this;
    }

    public TntBmsClient payload(PayloadCallback payloadCallback) {
        this.payloadCallback = payloadCallback;
        return this;
    }

    public TntBmsClient token(List<String> tokens) {
        this.tokens = tokens;
        return this;
    }

    public TntBmsClient token(TokenCallback tokenCallback) {
        this.tokenCallback = tokenCallback;
        return this;
    }

    public TntBmsClient maxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
        return this;
    }

    public TntBmsClient replaceOnExist(boolean isReplaceOnExists) {
        this.replaceOnExist = isReplaceOnExists;
        return this;
    }

    public Message build() {

        Message message = new Message();

        message.setMsgId(msgId);
        message.setMsgType(msgType);

        if(payloadCallback!=null) {
            message.setPayload(payloadCallback.prepare());
        } else {
            message.setPayload(payload);
        }

        if(tokenCallback!=null) {
            tokens = tokenCallback.prepare();
        }

        if(tokens!=null) {
            message.setTokens(tokens.toArray(new String[0]));
        }

        message.setMaxRetry(maxRetry);
        message.setReplaceOnExist(replaceOnExist);

        message.checkField();

        return message;

    }


}
