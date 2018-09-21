package com.lvmama.tnt.bms.admin.web.manage.domain;

import java.io.Serializable;

/**
 *
 */
public class RequestDTO<T> implements Serializable{
    private static final long serialVersionUID = -1491480517231555174L;
    private int pageNo;
    private int pageSize;
    private T request;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }
}
