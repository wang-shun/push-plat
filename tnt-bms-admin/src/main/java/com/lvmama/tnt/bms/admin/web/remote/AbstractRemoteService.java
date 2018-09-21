package com.lvmama.tnt.bms.admin.web.remote;

import com.lvmama.tnt.bms.api.domain.ResponseDTO;

/**
 *
 */
public abstract class AbstractRemoteService {
    
    private enum ResponseCode{
        SUCCESS("10000", "成功"),
        FAIL("10001","失败");
        
        private String code;
        private String message;

        private ResponseCode(String code, String messsage) {
            this.code = code;
            this.message = messsage;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public ResponseDTO returnSuccess() {
        ResponseDTO responseDTO = new ResponseDTO(true,ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        return responseDTO;
    }

    public ResponseDTO returnSuccess(String msg) {
        ResponseDTO responseDTO = new ResponseDTO(true, ResponseCode.SUCCESS.getCode(), msg);
        return responseDTO;
    }

    public <T> ResponseDTO returnSuccess(String msg, T result) {
        ResponseDTO responseDTO = new ResponseDTO(true, ResponseCode.SUCCESS.getCode(), msg, result);
        return responseDTO;
    }

    public ResponseDTO returnFailed(String msg) {
        ResponseDTO responseDTO = new ResponseDTO(false, ResponseCode.FAIL.getCode(), msg);
        return responseDTO;
    }
}
