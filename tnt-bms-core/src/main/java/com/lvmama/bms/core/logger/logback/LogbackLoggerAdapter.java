package com.lvmama.bms.core.logger.logback;

import ch.qos.logback.classic.LoggerContext;
import com.lvmama.bms.core.commons.utils.Assert;
import com.lvmama.bms.core.logger.Level;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerAdapter;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.File;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 *
 */
public class LogbackLoggerAdapter implements LoggerAdapter {

    private LoggerContext loggerContext;
    private ch.qos.logback.classic.Logger root;

    public LogbackLoggerAdapter() {
        loggerContext = getLoggerContext();
        root = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    private void configurate(ch.qos.logback.classic.Logger logger) {
    }

    @Override
    public Logger getLogger(Class<?> key) {
        return new LogbackLogger(loggerContext.getLogger(key));
    }

    @Override
    public Logger getLogger(String key) {
        return new LogbackLogger(loggerContext.getLogger(key));
    }

    @Override
    public void setLevel(Level level) {
        root.setLevel(toLogbackLevel(level));
    }

    @Override
    public Level getLevel() {
        return fromLogbackLevel(root.getLevel());
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public void setFile(File file) {

    }

    private LoggerContext getLoggerContext() {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        Assert.isInstanceOf(LoggerContext.class, factory,
                String.format(
                        "LoggerFactory is not a Logback LoggerContext but Logback is on "
                                + "the classpath. Either remove Logback or the competing "
                                + "implementation (%s loaded from %s). If you are using "
                                + "WebLogic you will need to add 'org.slf4j' to "
                                + "prefer-application-packages in WEB-INF/weblogic.xml",
                        factory.getClass(), getLocation(factory)));
        return (LoggerContext) factory;
    }

    private Object getLocation(ILoggerFactory factory) {
        try {
            ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                return codeSource.getLocation();
            }
        } catch (SecurityException ex) {
            // Unable to determine location
        }
        return "unknown location";
    }

    private static ch.qos.logback.classic.Level toLogbackLevel(Level level) {
        if (level == Level.ALL)
            return ch.qos.logback.classic.Level.ALL;
        if (level == Level.TRACE)
            return ch.qos.logback.classic.Level.TRACE;
        if (level == Level.DEBUG)
            return ch.qos.logback.classic.Level.DEBUG;
        if (level == Level.INFO)
            return ch.qos.logback.classic.Level.INFO;
        if (level == Level.WARN)
            return ch.qos.logback.classic.Level.WARN;
        if (level == Level.ERROR)
            return ch.qos.logback.classic.Level.ERROR;
        return ch.qos.logback.classic.Level.OFF;
    }


    private static Level fromLogbackLevel(ch.qos.logback.classic.Level level) {
        if (level == ch.qos.logback.classic.Level.ALL)
            return Level.ALL;
        if (level == ch.qos.logback.classic.Level.TRACE)
            return Level.TRACE;
        if (level == ch.qos.logback.classic.Level.DEBUG)
            return Level.DEBUG;
        if (level == ch.qos.logback.classic.Level.INFO)
            return Level.INFO;
        if (level == ch.qos.logback.classic.Level.WARN)
            return Level.WARN;
        if (level == ch.qos.logback.classic.Level.ERROR)
            return Level.ERROR;
        return Level.OFF;
    }

}
