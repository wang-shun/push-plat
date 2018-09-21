package com.lvmama.bms.core.domain.dto;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lvmama.bms.core.commons.utils.BeanUtils;
import com.lvmama.bms.core.commons.utils.DateUtils;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.domain.enums.EncryptMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class MessageDTO {

    /**
     * 内部消息Id
     */
    private Long id;

    /**
     * 内部消息-接收方Id
     */
    private Long msgTokenId;

    /**
     * 业务消息Id
     */
    private String msgId;

    /**
     * 消息类型Id
     */
    private Integer msgTypeId;

    private String msgType;

    private Object msgContent;

    private Integer maxRetry;

    private Integer retryCount;

    private String token;

    private Integer pushType;

    private String pushUrl;

    private Integer converterType;

    private Integer threhold;

    private Integer priority;

    private Date receiveTime;

    private boolean isSpeedFast;

    private Integer rpcPusherId;

    private Integer convertMapId;

    private String encryptMethod;

    private String encryptKey;

    private Integer connectTimeOut;

    private Integer readTimeOut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMsgTokenId() {
        return msgTokenId;
    }

    public void setMsgTokenId(Long msgTokenId) {
        this.msgTokenId = msgTokenId;
    }

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

    public Integer getMaxRetry() {
        if(maxRetry == null) {
            return 0;
        }
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public Integer getConverterType() {
        return converterType;
    }

    public void setConverterType(Integer converterType) {
        this.converterType = converterType;
    }

    public Integer getThrehold() {
        return threhold;
    }

    public void setThrehold(Integer threhold) {
        this.threhold = threhold;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public boolean isSpeedFast() {
        return isSpeedFast;
    }

    public void setSpeedFast(boolean speedFast) {
        isSpeedFast = speedFast;
    }

    public Integer getRpcPusherId() {
        return rpcPusherId;
    }

    public void setRpcPusherId(Integer rpcPusherId) {
        this.rpcPusherId = rpcPusherId;
    }

    public Integer getConvertMapId() {
        return convertMapId;
    }

    public void setConvertMapId(Integer convertMapId) {
        this.convertMapId = convertMapId;
    }

    public String getEncryptMethod() {
        return encryptMethod;
    }

    public void setEncryptMethod(String encryptMethod) {
        this.encryptMethod = encryptMethod;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Integer connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public Integer getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(Integer readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public Integer getRetryCount() {
        if(retryCount == null) {
            return 0;
        }
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String clearMsgContent(){
        return
                "id=" + id +
                "|msgTokenId=" + msgTokenId +
                "|msgId=" + msgId +
                "|msgTypeId=" + msgTypeId +
                "|msgType=" + msgType +
                "|maxRetry=" + maxRetry +
                "|retryCount=" + retryCount +
                "|token=" + token +
                "|pushType=" + pushType +
                "|pushUrl=" + pushUrl +
                "|converterType=" + converterType +
                "|threhold=" + threhold +
                "|priority=" + priority +
                "|receiveTime=" + DateUtils.format(receiveTime, "yyyy-MM-dd HH:mm:ss.SSS") +
                "|isSpeedFast=" + isSpeedFast +
                "|rpcPusherId=" + rpcPusherId +
                "|convertMapId=" + convertMapId +
                "|encryptMethod=" + encryptMethod +
                "|connectTimeOut="+ connectTimeOut +
                "|readTimeOut=" + readTimeOut +
                "|msgContent="
                ;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                "|msgTokenId=" + msgTokenId +
                "|msgId=" + msgId +
                "|msgTypeId=" + msgTypeId +
                "|msgType=" + msgType +
                "|maxRetry=" + maxRetry +
                "|retryCount=" + retryCount +
                "|token=" + token +
                "|pushType=" + pushType +
                "|pushUrl=" + pushUrl +
                "|converterType=" + converterType +
                "|threhold=" + threhold +
                "|priority=" + priority +
                "|receiveTime=" + DateUtils.format(receiveTime, "yyyy-MM-dd HH:mm:ss.SSS") +
                "|isSpeedFast=" + isSpeedFast +
                "|rpcPusherId=" + rpcPusherId +
                "|convertMapId=" + convertMapId +
                "|encryptMethod=" + encryptMethod +
                "|connectTimeOut="+ connectTimeOut +
                "|readTimeOut=" + readTimeOut +
                "|msgContent=" + msgContent
                ;
    }
}
