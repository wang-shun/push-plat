package com.lvmama.bms.extend.rpc;

public enum  Result {
    SUCCESS,    // 执行成功
    FAILED,     // 执行失败
    EXCEPTION  // 执行异常, 这种情况会重试
}
