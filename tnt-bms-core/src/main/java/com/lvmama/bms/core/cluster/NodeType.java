package com.lvmama.bms.core.cluster;

import com.lvmama.bms.core.commons.utils.StringUtils;

/**
 * @author Robert HG (254963746@qq.com) on 6/22/14.
 */
public enum NodeType {

    // job tracker
    MSG_SCHEDULER,
    // task tracker
    MSG_PUSHER,
    // client
    MSG_CLIENT,
    // monitor
    MONITOR,

    BACKEND;

    public static NodeType convert(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return NodeType.valueOf(value);
    }
}
