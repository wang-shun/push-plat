package com.lvmama.tnt.bms.admin.web.domain.po;

import java.util.Date;

public class MessageTokenDO {
    /**
    tnt_bms_msg_token*msg_id
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     */
    private String msgId;

    /**
    tnt_bms_msg_token*token
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     */
    private String token;

    /**
    tnt_bms_msg_token*status
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     */
    private Byte status;

    /**
    tnt_bms_msg_token*modify_time
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     */
    private Date modifyTime;

    /**
    tnt_bms_msg_token*now_retry
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     */
    private Short nowRetry;

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @return the value of tnt_bms_msg_token.msg_id
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @param msgId the value for tnt_bms_msg_token.msg_id
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @return the value of tnt_bms_msg_token.token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @param token the value for tnt_bms_msg_token.token
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @return the value of tnt_bms_msg_token.status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @param status the value for tnt_bms_msg_token.status
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @return the value of tnt_bms_msg_token.modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @param modifyTime the value for tnt_bms_msg_token.modify_time
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @return the value of tnt_bms_msg_token.now_retry
     */
    public Short getNowRetry() {
        return nowRetry;
    }

    /**
     *
     * @author Administrator
     * @date 2017-11-09 16:01:47
     * @param nowRetry the value for tnt_bms_msg_token.now_retry
     */
    public void setNowRetry(Short nowRetry) {
        this.nowRetry = nowRetry;
    }
}