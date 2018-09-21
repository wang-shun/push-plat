package com.lvmama.bms.core.domain.enums;

/**
 * 消息状态
 */
public enum MsgStatus {
    RECEIVE, //接收
    SENDING, //发送中
    SEND_SUCCESS, //发送成功
    SEND_FAIL, //发送失败
    INVALID //无效
}
