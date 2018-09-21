package com.lvmama.tnt.bms.admin.web.util;


import org.springframework.util.StringUtils;

/**
 * @author longhr
 * @version 2017/11/9 0009
 */
public class BmsUtil {

    public static String getUUID() {
        return StringUtils.replace(java.util.UUID.randomUUID().toString(), "-", "").toUpperCase();
    }

    public static boolean isEmpty(String cs) {
        return cs == null || cs.length() == 0 || "null".equals(cs);
    }
}
