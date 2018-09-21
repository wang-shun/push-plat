package com.lvmama.tnt.bms.api.domain;

import java.io.Serializable;

/**
 * 接入内容
 */
public class NewsAccessDTO implements Serializable {
    private static final long serialVersionUID = 8889614282843253118L;
    /**
     * 主键 id
     */
    private Long id;
    /**
     * 组ID
     */
    private Long groupId;
    /**
     * 接入成功会返回一个token
     */
    private String token;
    /**
     * 接入名称
     */
    private String name;
    /**
     * 接收类型，1：推送，2：拉取
     */
    private Integer receiveType;
    /**
     * 推送类型，1：HTTP，2：TCP，3：RPC
     */
    private Integer pushType;
    /**
     *  	推送接口地址
     */
    private String pushUrl;
    /**
     * 开关，1：开启（默认），0：关闭
     */
    private Integer opened;
    /**
     * 转换器，1：json，2：xml，3：form
     */
    private Integer converterType;
    /**
     * 推送优先级，1-10个优先级，默认为5
     */
    private Integer priority;

    /**
     * 拉取频率或发送频率限制，接入时评估推送URL能承受的压力
     */
    private Integer threshold;
    /**
     * 推送器ID
     */
    private Integer pushID;
    /**
     * 映射器ID
     */
    private Integer convertID;
    /**
     * 推送给分销商时组装数据的格式
     */
    private String convertMap;

    /**
     * 成员需要加入分组，必须填写分组的token，不需要加入分组则不必填写，须和下面的接口模式一致
     */
    private String groupToken;
    /**
     * 成员需要加入分组，必须填写分组的token，不需要加入分组则不必填写，须和下面的接口模式一致
     */
    private String mode;

    private String encryptKey;
    private String encryptMethod;

    /**
     * 连接超时设置
     */
    private Integer connectTimeOut;

    /**
     * 读取超时
     */
    private Integer readTimeOut;

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

    public String getGroupToken() {
        return groupToken;
    }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getConvertMap() {
        return convertMap;
    }

    public void setConvertMap(String convertMap) {
        this.convertMap = convertMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
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

    public Integer getPushID() {
        return pushID;
    }

    public void setPushID(Integer pushID) {
        this.pushID = pushID;
    }

    public Integer getConvertID() {
        return convertID;
    }

    public void setConvertID(Integer convertID) {
        this.convertID = convertID;
    }

    @Override
    public String toString() {
        return "NewsAccessDTO{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", receiveType=" + receiveType +
                ", pushType=" + pushType +
                ", pushUrl='" + pushUrl + '\'' +
                ", opened=" + opened +
                ", converterType=" + converterType +
                ", priority=" + priority +
                ", threshold=" + threshold +
                ", pushID=" + pushID +
                ", convertID=" + convertID +
                ", convertMap='" + convertMap + '\'' +
                ", groupToken='" + groupToken + '\'' +
                ", mode='" + mode + '\'' +
                ", encryptKey='" + encryptKey + '\'' +
                ", encryptMethod='" + encryptMethod + '\'' +
                ", connectTimeOut=" + connectTimeOut +
                ", readTimeOut=" + readTimeOut +
                '}';
    }
}
