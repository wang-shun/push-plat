package com.lvmama.bms.pusher.store.domain;

public class ConvertMapPO {

    private Integer id;

    private String requestMap;

    private String responseMap;

    private Integer generation;

    private String httpHeader;

    private Integer version;

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(String requestMap) {
        this.requestMap = requestMap;
    }

    public String getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(String responseMap) {
        this.responseMap = responseMap;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return"id=" + id +
                "|requestMap='" + requestMap +
                "|responseMap='" + responseMap +
                "|generation=" + generation +
                "|version=" + version;
    }
}
