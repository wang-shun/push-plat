package com.lvmama.bms.core.logger.support;

import com.lvmama.bms.core.logger.Logger;

/**
 * 扩展多参数Logger
 * @author Robert HG (254963746@qq.com) on 5/19/15.
 */
public abstract class AbstractLogger implements Logger {

    private final static int THRESHOLD = 2000;

    protected final static String CATALINA = "logType=CATALINA|";
    protected final static String BIZEVENT = "logType=BIZEVENT|";
    protected final static String NORMAL = "logType=NORMAL|";

    protected boolean isNormal(String message) {
        return message == null | message.length() < THRESHOLD;
    }

    protected String truncate(String message) {
        return isNormal(message) ? message : message.substring(0, THRESHOLD);
    }


    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            trace(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            debug(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            info(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            warn(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            error(ft.getMessage(), ft.getThrowable());
        }
    }
}
