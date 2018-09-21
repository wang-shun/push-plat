package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
public class BaseController {

    public final static String SUCCESS_CODE = "10000";
    public final static String FAILED_CODE = "10001";

    public ResponseVO returnSuccess() {
        ResponseVO responseVO = new ResponseVO(true, SUCCESS_CODE, "");
        return responseVO;
    }

    public ResponseVO returnSuccess(String msg) {
        ResponseVO responseVO = new ResponseVO(true, SUCCESS_CODE, msg);
        return responseVO;
    }

    public <T> ResponseVO returnSuccess(String msg, T result) {
        ResponseVO responseVO = new ResponseVO(true, SUCCESS_CODE, msg, result);
        return responseVO;
    }

    public ResponseVO returnFailed(String msg) {
        ResponseVO responseVO = new ResponseVO(false, FAILED_CODE, msg);
        return responseVO;
    }
}
