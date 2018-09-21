package com.lvmama.tnt.bms.admin.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
public class BmsDateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public static String getTimeFormat(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static Long getTimeMillis(String formatTime) {
        try {
            Date date = sdf.parse(formatTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFourHoursAgoTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 4);
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(calendar.getTime());
    }

    public static String getOneDayBeforeTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(calendar.getTime());
    }

}
