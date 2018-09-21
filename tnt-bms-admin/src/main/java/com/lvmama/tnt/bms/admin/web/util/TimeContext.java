package com.lvmama.tnt.bms.admin.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class TimeContext {
    private static final Logger logger = LoggerFactory.getLogger(TimeContext.class);
    public static final ThreadLocal<List<Long>> times = new ThreadLocal<>();

    public static void record() {
        times.get().add(System.currentTimeMillis());
    }

    public static void calculateTime() {
        List<Long> time = times.get();
        int len = time.size();
        for (int i = len-1; i > 1; i++) {
            logger.info(String.valueOf((time.get(i) - time.get(i-1))));
        }
    }
}
