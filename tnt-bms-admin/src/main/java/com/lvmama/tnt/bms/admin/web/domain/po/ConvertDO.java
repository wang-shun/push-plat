package com.lvmama.tnt.bms.admin.web.domain.po;

import java.io.Serializable;

/**
 *
 */
public class ConvertDO implements Serializable {

    private static final long serialVersionUID = -6871234295540135818L;

    private Integer id;
    private String name;
    private Integer generation;
    private String requestMap;
    private String responseMap;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
