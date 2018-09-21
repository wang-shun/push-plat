package com.lvmama.tnt.bms.api.domain;

import java.io.Serializable;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
public class ResponseDTO<T> implements Serializable{
    private static final long serialVersionUID = -1065484098861021776L;

    public ResponseDTO(){
        this.success = true;
    }

    public ResponseDTO(T result){
        this.success = true;
        this.result = result;
    }

    public ResponseDTO(boolean success, String resCode, String resMsg) {
        this.success = success;
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public ResponseDTO(boolean success, String resCode, String resMsg, T result) {
        this.success = success;
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.result = result;
    }

    private boolean success;
    private String resCode;
    private String resMsg;
    private long totalCount;
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
