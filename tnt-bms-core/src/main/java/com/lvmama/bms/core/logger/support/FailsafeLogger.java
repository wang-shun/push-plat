package com.lvmama.bms.core.logger.support;

import com.lvmama.bms.core.commons.utils.NetUtils;
import com.lvmama.bms.core.logger.ElkLogAspect;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.support.Version;

public class FailsafeLogger extends AbstractLogger implements Logger {

    private Logger logger;

    public FailsafeLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private String appendContextMessage(String msg) {
//        return msg + "|bms-version=" + Version.getVersion() + "|current-host=" + NetUtils.getLocalHost();
        return msg;
    }

    public void trace(String msg, Throwable e) {
        try {
            logger.trace(appendContextMessage(msg), e);
        } catch (Throwable ignored) {
        }
    }

    public void trace(Throwable e) {
        try {
            logger.trace(e);
        } catch (Throwable ignored) {
        }
    }

    public void trace(String msg) {
        try {
            logger.trace(appendContextMessage(msg));
        } catch (Throwable ignored) {
        }
    }

    public void debug(String msg, Throwable e) {
        try {
            logger.debug(appendContextMessage(msg), e);
        } catch (Throwable ignored) {
        }
    }

    public void debug(Throwable e) {
        try {
            logger.debug(e);
        } catch (Throwable ignored) {
        }
    }

    public void debug(String msg) {
        try {
            logger.debug(appendContextMessage(msg));
        } catch (Throwable ignored) {
        }
    }

    public void info(String msg, Throwable e) {
        try {
            logger.info(appendContextMessage(msg), e);
        } catch (Throwable ignored) {
        }
    }

    public void info(String msg) {
        try {
            logger.info(appendContextMessage(msg));
        } catch (Throwable ignored) {
        }
    }

    public void warn(String msg, Throwable e) {
        try {
            logger.warn(appendContextMessage(wrap("warn",msg)), e);
        } catch (Throwable ignored) {
        }
    }

    public void warn(String msg) {
        try {
            logger.warn(appendContextMessage(wrap("warn",msg)));
        } catch (Throwable ignored) {
        }
    }

    private String wrap(String key, String message) {
//        return key + "={" + message + "}";
        return message;
    }


    public void error(String msg, Throwable e) {
        try {
            logger.error(appendContextMessage(wrap("error",msg)), e);
        } catch (Throwable ignored) {
        }
    }

    public void error(String msg) {
        try {
            logger.error(appendContextMessage(wrap("error",msg)));
        } catch (Throwable ignored) {
        }
    }

    public void error(Throwable e) {
        try {
            logger.error(e);
        } catch (Throwable ignored) {
        }
    }

    public void info(Throwable e) {
        try {
            logger.info(e);
        } catch (Throwable ignored) {
        }
    }

    public void warn(Throwable e) {
        try {
            logger.warn(e);
        } catch (Throwable ignored) {
        }
    }

    public boolean isTraceEnabled() {
        try {
            return logger.isTraceEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isDebugEnabled() {
        try {
            return logger.isDebugEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isInfoEnabled() {
        try {
            return logger.isInfoEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isWarnEnabled() {
        try {
            return logger.isWarnEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isErrorEnabled() {
        try {
            return logger.isErrorEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

}