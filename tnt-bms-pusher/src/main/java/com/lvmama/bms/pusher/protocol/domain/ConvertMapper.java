package com.lvmama.bms.pusher.protocol.domain;

import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.pusher.map.v1.vo.DataMap;
import com.lvmama.bms.pusher.map.vo.ResponseMap;

public class ConvertMapper {

    private ConvertVersion convertVersion;

    private String httpHeader;

    private String requestMap; //v2

    private ResponseMap responseMap; //v1+v2

    private DataMap dataMap; //v1

    private Integer version; //数据版本

    public ConvertMapper(DataMap dataMap, ResponseMap responseMap, ConvertVersion convertVersion, Integer version) {
        this.dataMap = dataMap;
        this.responseMap = responseMap;
        this.convertVersion = convertVersion;
        this.version = version;
    }

    public ConvertMapper(String requestMap, ResponseMap responseMap, ConvertVersion convertVersion, Integer version, String httpHeader) {
        this.requestMap = requestMap;
        this.responseMap = responseMap;
        this.convertVersion = convertVersion;
        this.version = version;
        this.httpHeader = httpHeader;
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
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

    public ResponseMap getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(ResponseMap responseMap) {
        this.responseMap = responseMap;
    }

    public ConvertVersion getConvertVersion() {
        return convertVersion;
    }

    public void setConvertVersion(ConvertVersion convertVersion) {
        this.convertVersion = convertVersion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "convertVersion=" + convertVersion +
                "|requestMap=" + requestMap +
                "|responseMap=" + responseMap +
                "|dataMap=" + dataMap +
                "|version=" + version;
    }
}
