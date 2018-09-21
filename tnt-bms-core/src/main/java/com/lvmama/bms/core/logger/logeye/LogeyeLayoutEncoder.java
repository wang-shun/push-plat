package com.lvmama.bms.core.logger.logeye;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class LogeyeLayoutEncoder extends PatternLayoutEncoder {

    private static final DateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    private static final String LVLOG_PRE = "[LvLogS";

    private static final String LVLOG_SUFFIX = "[LvLogE";

    private String hostName;
    private String appName;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        LogTrackContext.appName = appName;
    }

    public LogeyeLayoutEncoder() {
        super();
        hostName = getHostName();
    }

    public byte[] encode(ILoggingEvent event) {
        String txt = this.layout.doLayout(event);
        return convertToBytes(lvLogEyeLayout(event, txt));

    }

    protected String lvLogEyeLayout(ILoggingEvent iLoggingEvent, String originTxt)  {

        setAppName(iLoggingEvent.getLoggerContextVO().getName());

        Date now=new Date();

        StringBuilder sbuf = addTextPre(iLoggingEvent, now);

        sbuf.append(addTextSuffix(iLoggingEvent, replaceEnter(new StringBuilder(originTxt)), now).toString());
        sbuf.append(System.getProperty("line.separator"));

        return sbuf.toString();
    }

    // 替换orignSb 最后的回车换行
    protected String replaceEnter(StringBuilder orignSb) {

        int index = -1;
        String orignStr = orignSb.toString();
        int orignStrLen = orignStr.length();

        if((index = orignStr.lastIndexOf("\r\n")) > -1 ) {
            // window
            if(orignStrLen - 2 == index) {
                orignStr =  orignStr.substring(0, index);
            }
        } else if((index =orignStr.lastIndexOf("\r")) > -1) {
            // mac
            if(orignStrLen - 1 == index) {
                orignStr = orignStr.substring(0, index);
            }
        } else if((index =orignStr.lastIndexOf("\n")) > -1) {
            // linux
            if(orignStrLen - 1 == index) {
                orignStr = orignStr.substring(0, index);
            }
        }


        return orignStr.toString();
    }

    // 原数据 之后需要加的 字符串
    protected StringBuilder addTextSuffix(ILoggingEvent iLoggingEvent, String txtOrign,  Date now) {

        String trackNumber = "track:\""+ StringUtils.trimToEmpty(LogTrackContext.getTrackNumber())+"\"";
        String level = "LVL:\""+getLevelStr(iLoggingEvent)+"\"";
        String hostName = "HOST:\""+getHostName()+"\"";
        String appName="APP:\""+StringUtils.trimToEmpty(this.appName)+"\"";
        String parentAppName="PAPP:\""+StringUtils.trimToEmpty(LogTrackContext.getParentAppName())+"\"";
        String className = "CLASS:\""+iLoggingEvent.getLoggerName()+"\"";
        String dateTime="TIME:\""+now.getTime()+"\"";

        StringBuilder tailText = new StringBuilder();
        if(txtOrign != null) {
            tailText.append(txtOrign);
        }

        tailText.append(LVLOG_SUFFIX);
        tailText.append(" " + dateTime);
        tailText.append(" " + level);
        tailText.append(" " + appName);
        tailText.append(" " + parentAppName);
        tailText.append(" " + hostName);
        tailText.append(" " + trackNumber);
        tailText.append(" " + className);

        tailText.append("]");


        return tailText;
    }

    private String getHostName() {
        BufferedReader br = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("hostname");
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return br.readLine();
        } catch (Throwable e) {
            return ExceptionUtils.rethrow(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ExceptionUtils.rethrow(e);
                }
            }
        }
    }

    private String getLevelStr(ILoggingEvent event) {
        return event.getLevel().toString();
    }

    // 原数据 之前需要加的 字符串
    protected StringBuilder addTextPre(ILoggingEvent event, Date now) {

        StringBuilder newText = new StringBuilder();

        newText.append(format.format(now));
        newText.append(" "+getLevelStr(event));
        newText.append(LVLOG_PRE);
        newText.append("]");

        return newText;
    }

    private byte[] convertToBytes(String s) {
        if(getCharset() == null) {
            return s.getBytes();
        } else {
            try {
                return s.getBytes(getCharset().name());
            } catch (UnsupportedEncodingException var3) {
                throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
            }
        }
    }
}
