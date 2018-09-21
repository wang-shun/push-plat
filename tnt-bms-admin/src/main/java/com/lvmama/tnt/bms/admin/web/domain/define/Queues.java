package com.lvmama.tnt.bms.admin.web.domain.define;

/**
 *
 */
public class Queues {

    /**
     * 映射器配置队列
     */
    public static final String CONVERT_DESTINATION = "TNT_BMS_CONVERT_CONFIG";

    /**
     * 推动器配置队列
     */
    public static final String PUSHER_DESTINATION = "TNT_BMS_PUSHER_CONFIG";
    /**
     * 手动发送队列
     */
    public static final String MANUAL_DESTINATION = "TNT_BMS_MANUAL_SEND";
    /**
     * 配置同步队列
     */
    public static final String ACCESS_DESTINATION = "TNT_BMS_ACTIVEMQ_CONFIG_MESSAGE";

}
