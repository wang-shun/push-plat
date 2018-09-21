package com.lvmama.bms.scheduler.store.domain.po;

import com.lvmama.bms.core.domain.enums.EncryptMethod;

public class TokenPO {

    private Integer id;

    private String token;

    private String name;

    private Integer receiveType;

    private Integer pushType;

    private String pushUrl;

    private Integer isOpen;

    private Integer converterType;

    private Integer priority;

    private Integer threshold;

    private Float failRate;

    private Integer version;

    private Integer groupId;

    private Integer rpcPusherId;

    private Integer convertId;

    private String encryptKey;

    private String encryptMethod;

    private Integer connectTimeOut;

    private Integer readTimeOut;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Integer receiveType) {
        this.receiveType = receiveType;
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

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getConverterType() {
        return converterType;
    }

    public void setConverterType(Integer converterType) {
        this.converterType = converterType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Float getFailRate() {
        return failRate;
    }

    public void setFailRate(Float failRate) {
        this.failRate = failRate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRpcPusherId() {
        return rpcPusherId;
    }

    public void setRpcPusherId(Integer rpcPusherId) {
        this.rpcPusherId = rpcPusherId;
    }

    public Integer getConvertId() {
        return convertId;
    }

    public void setConvertId(Integer convertId) {
        this.convertId = convertId;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getEncryptMethod() {
        return encryptMethod;
    }

    public void setEncryptMethod(String encryptMethod) {
        this.encryptMethod = encryptMethod;
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
}
