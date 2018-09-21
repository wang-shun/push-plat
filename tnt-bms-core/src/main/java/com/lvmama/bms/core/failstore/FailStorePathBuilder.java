package com.lvmama.bms.core.failstore;

import com.lvmama.bms.core.AppContext;

/**
 * @author Robert HG (254963746@qq.com) 4/1/16.
 */
public class FailStorePathBuilder {

    public static String getBizLoggerPath(AppContext appContext) {
        return getStorePath(appContext) + "/bizlog_failstore/";
    }

    public static String getJobFeedbackPath(AppContext appContext) {
        return getStorePath(appContext) + "/msg_feedback_failstore/";
    }

    public static String getJobSubmitFailStorePath(AppContext appContext) {
        return getStorePath(appContext) + "/msg_submit_failstore/";
    }

    public static String getDepJobSubmitFailStorePath(AppContext appContext) {
        return getStorePath(appContext) + "/dep_msg_submit_failstore/";
    }

    private static String getStorePath(AppContext appContext) {
        return appContext.getConfig().getDataPath()
                + "/.bms" + "/" +
                appContext.getConfig().getNodeType() + "/" +
                appContext.getConfig().getNodeGroup();
    }
}
