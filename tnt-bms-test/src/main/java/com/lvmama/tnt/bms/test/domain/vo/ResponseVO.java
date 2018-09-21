package com.lvmama.tnt.bms.test.domain.vo;

import java.io.Serializable;

/**
 *
 */
public class ResponseVO implements Serializable {

    private String respCode;
    private String respMsg;

    public ResponseVO() {

    }

    public ResponseVO(String respCode, String respMsg) {
        this.respCode = respCode;
        this.respMsg = respMsg;
    }
    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
