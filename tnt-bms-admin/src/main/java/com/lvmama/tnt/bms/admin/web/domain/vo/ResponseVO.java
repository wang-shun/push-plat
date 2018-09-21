package com.lvmama.tnt.bms.admin.web.domain.vo;

import java.io.Serializable;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
public class ResponseVO<T> implements Serializable{
    private static final long serialVersionUID = -1065484098861021776L;

    public ResponseVO(){
        this.success = true;
    }

    public ResponseVO(boolean success, String resCode, String resMsg) {
        this.success = success;
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public ResponseVO(boolean success, String resCode, String resMsg, T result) {
        this.success = success;
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.result = result;
    }

    private boolean success;
    private String resCode;
    private String resMsg;
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
}
