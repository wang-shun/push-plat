package com.lvmama.bms.core.logger.logeye;

import java.util.UUID;

/**
 *
 */
public class LogTrackContext {
    public static final String TRACENUMBER = "_trackNumber";
    static final ThreadLocal<String> trackNumberContext = new ThreadLocal();
    static final ThreadLocal<String> parentAppNameContext = new ThreadLocal();
    public static String appName;

    public LogTrackContext() {
    }

    public static void setTrackNumber(String trackNumber) {
        trackNumberContext.set(trackNumber);
    }

    public static void setParentAppName(String parentAppName) {
        parentAppNameContext.set(parentAppName);
    }

    public static String getTrackNumber() {
        return (String)trackNumberContext.get();
    }

    public static String getParentAppName() {
        return (String)parentAppNameContext.get();
    }

    public static void remove() {
        try {
            trackNumberContext.remove();
        } catch (Exception var2) {
            ;
        }

        try {
            parentAppNameContext.remove();
        } catch (Exception var1) {
            ;
        }

    }

    public static void initTrackNumber() {
        trackNumberContext.set(getRandom18String());
    }

    private static String random() {
        return UUID.randomUUID().toString();
    }

    public static String getRandom18String() {
        Long millis = Long.valueOf(System.currentTimeMillis());
        Integer n = Integer.valueOf((int)(Math.random() * 100000.0D));
        int zeroNumber = 5 - n.toString().length();
        String pre = "";

        for(int r = 0; r < zeroNumber; ++r) {
            pre = pre + "0";
        }

        String var5 = millis.toString();
        if(!pre.equals("")) {
            var5 = var5 + pre + n.toString();
        } else {
            var5 = var5 + n.toString();
        }

        return Long.toHexString(Long.parseLong(var5));
    }
}
