package com.lvmama.bms.core.logger.logback;

import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.support.AbstractLogger;

/**
 *
 */
public class LogbackLogger extends AbstractLogger implements Logger {
    private final ch.qos.logback.classic.Logger logger;

    public LogbackLogger(ch.qos.logback.classic.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void trace(String msg) {
        logger.trace(NORMAL+msg);
    }

    @Override
    public void trace(Throwable e) {
        logger.trace(NORMAL,e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        logger.trace(NORMAL+msg,e);
    }

    @Override
    public void debug(String msg) {
        logger.debug(NORMAL+msg);
    }

    @Override
    public void debug(Throwable e) {
        logger.debug(NORMAL, e);
    }

    @Override
    public void debug(String msg, Throwable e) {
        logger.debug(NORMAL+msg, e);
    }

    @Override
    public void info(String msg) {
        if (isNormal(msg)) {
            info(NORMAL + msg);
        } else {
            info(BIZEVENT + truncate(msg));
            info(CATALINA+msg);
        }
    }

    @Override
    public void info(Throwable e) {
        logger.info(NORMAL, e);
    }

    @Override
    public void info(String msg, Throwable e) {
        if (isNormal(msg)) {
            info(NORMAL + msg, e);
        } else {
            info(BIZEVENT + truncate(msg), e);
            info(CATALINA+msg, e);
        }
    }

    @Override
    public void warn(String msg) {
        logger.warn(NORMAL+msg);
    }

    @Override
    public void warn(Throwable e) {
        logger.warn(NORMAL, e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        logger.warn(NORMAL+msg, e);
    }

    @Override
    public void error(String msg) {
        logger.error(NORMAL+msg);
    }

    @Override
    public void error(Throwable e) {
        logger.error(NORMAL, e);
    }

    @Override
    public void error(String msg, Throwable e) {
        logger.error(NORMAL+msg, e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }
}
