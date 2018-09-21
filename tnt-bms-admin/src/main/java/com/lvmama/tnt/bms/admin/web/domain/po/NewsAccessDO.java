package com.lvmama.tnt.bms.admin.web.domain.po;

import java.util.List;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
public class NewsAccessDO extends BaseDO {
    private static final long serialVersionUID = -4178503870049309674L;

    private Long groupId;
    private String token;
    private String name;
    private Integer receiveType;
    private Integer pushType;
    private String pushUrl;
    private Integer opened;
    private Integer converterType;
    private Integer priority;
    private Integer connectTimeOut;
    private Integer readTimeOut;
    private Integer threshold;
    private Integer version;
    private String encryptKey;
    private String encryptMethod;

    private Integer pushID;
    private Integer convertID;

    private String groupToken;
    private String mode;

    private ConvertDO convertDO;
    private MsgPusherDO msgPusherDO;

    private List<NewsAccessDO> childrenNodes;

    private Integer rpcPusherId;

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

    public Integer getRpcPusherId() {
        return rpcPusherId;
    }

    public void setRpcPusherId(Integer rpcPusherId) {
        this.rpcPusherId = rpcPusherId;
    }

    public ConvertDO getConvertDO() {
        return convertDO;
    }

    public void setConvertDO(ConvertDO convertDO) {
        this.convertDO = convertDO;
    }

    public MsgPusherDO getMsgPusherDO() {
        return msgPusherDO;
    }

    public void setMsgPusherDO(MsgPusherDO msgPusherDO) {
        this.msgPusherDO = msgPusherDO;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<NewsAccessDO> getChildrenNodes() {
        return childrenNodes;
    }

    public void setChildrenNodes(List<NewsAccessDO> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getGroupToken() {
        return groupToken;
    }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
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
