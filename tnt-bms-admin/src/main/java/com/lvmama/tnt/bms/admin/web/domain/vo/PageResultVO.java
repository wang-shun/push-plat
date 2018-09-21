package com.lvmama.tnt.bms.admin.web.domain.vo;

import com.lvmama.tnt.bms.admin.web.domain.define.Constants;

import java.io.Serializable;

/**
 * @author longhr
 * @version 2017/11/1 0001
 */
public class PageResultVO<T> implements Serializable{
    private static final long serialVersionUID = 1335531353161987447L;

    public PageResultVO(){
        this.pageSize = Constants.PAGE_SIZE;
    }

    public PageResultVO(long totalCount, long totalPage, long pageNo){
        this.pageSize = Constants.PAGE_SIZE;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.pageNo = pageNo;
    }

    private long totalCount;
    private long totalPage;
    private long pageSize;
    private long pageNo;
    private T result;

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
