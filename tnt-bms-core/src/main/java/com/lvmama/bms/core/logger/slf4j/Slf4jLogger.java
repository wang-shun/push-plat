package com.lvmama.bms.core.logger.slf4j;


import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.support.AbstractLogger;
import sun.nio.cs.ext.MacCentralEurope;

import java.io.Serializable;

public class Slf4jLogger extends AbstractLogger implements Logger, Serializable {

    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public void trace(String msg) {
        logger.trace(NORMAL+msg);
    }

    public void trace(Throwable e) {
        logger.trace(NORMAL+e.getMessage(), e);
    }

    public void trace(String msg, Throwable e) {
        logger.trace(NORMAL+msg, e);
    }

    public void debug(String msg) {
        logger.debug(NORMAL+msg);
    }

    public void debug(Throwable e) {
        logger.debug(NORMAL+e.getMessage(), e);
    }

    public void debug(String msg, Throwable e) {
        logger.debug(NORMAL+msg, e);
    }

    public void info(String msg) {
        if (isNormal(msg)) {
            logger.info(NORMAL + msg);
        } else {
            logger.info(BIZEVENT + truncate(msg));
            logger.info(CATALINA+msg);
        }
    }

    public void info(Throwable e) {
        logger.info(NORMAL+e.getMessage(), e);
    }

    public void info(String msg, Throwable e) {
        if (isNormal(msg)) {
            logger.info(NORMAL + msg,e);
        } else {
            logger.info(BIZEVENT + truncate(msg),e);
            logger.info(CATALINA+msg, e);
        }
    }

    public void warn(String msg) {
        logger.warn(NORMAL+msg);
    }

    public void warn(Throwable e) {
        logger.warn(NORMAL+e.getMessage(), e);
    }

    public void warn(String msg, Throwable e) {
        logger.warn(NORMAL+msg, e);
    }

    public void error(String msg) {
        logger.error(NORMAL+msg);
    }

    public void error(Throwable e) {
        logger.error(NORMAL+e.getMessage(), e);
    }

    public void error(String msg, Throwable e) {
        logger.error(NORMAL+msg, e);
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

}
