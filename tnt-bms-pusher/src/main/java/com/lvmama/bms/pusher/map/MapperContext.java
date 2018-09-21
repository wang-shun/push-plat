package com.lvmama.bms.pusher.map;

import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.pusher.map.v1.vo.DataMap;

public class MapperContext {

    private Object payload;

    private ConvertVersion convertVersion;

    private DataMap dataMap; //v1

    private String requestMap; //v2

    private EncryptMethod encryptMethod;

    private String encryptKey;

    private String httpHeader;

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public ConvertVersion getConvertVersion() {
        return convertVersion;
    }

    public void setConvertVersion(ConvertVersion convertVersion) {
        this.convertVersion = convertVersion;
    }

    public DataMap getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMap dataMap) {
        this.dataMap = dataMap;
    }

    public String getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(String requestMap) {
        this.requestMap = requestMap;
    }

    public EncryptMethod getEncryptMethod() {
        return encryptMethod;
    }

    public void setEncryptMethod(EncryptMethod encryptMethod) {
        this.encryptMethod = encryptMethod;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }
}
