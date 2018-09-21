package com.lvmama.bms.core.domain;

/**
 * @author Robert HG (254963746@qq.com) on 6/13/15.
 */
public enum Action {

    EXECUTE_SUCCESS,    // 执行成功
    EXECUTE_FAILED,     // 执行失败
    EXECUTE_EXCEPTION,   // 执行异常, 这种情况会重试
    EXECUTE_RATE_LIMIT  //超过发送频率

}
