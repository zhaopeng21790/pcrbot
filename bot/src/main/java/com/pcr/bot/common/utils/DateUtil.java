package com.pcr.bot.common.utils;

import cn.hutool.core.date.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Codi
 * @date 2020/8/3
 **/
public class DateUtil {
    /**
     * 返回当前时间：yyyy-MM-dd GMT时区
     */
    public static String getCuttentDateOfDayString() {
        return getDate("yyyy-MM-dd");
    }
    public static String getCuttentDateOfMonthString() {
        return getDate("yyyy-MM");
    }

    private static String getDate(String formatString) {
        DateTime dateTime = cn.hutool.core.date.DateUtil.date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return dateTime.toString(simpleDateFormat);
    }


    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    /**
     * 返回当前时间：yyyy-MM-dd
     * 由于pcr一天的时间：2020-08-04 05:00:00 - 2020-08-05 05:00:00 统一为2020-08-04
     */
    public static String getPCRDateOfDayString() {
        long pcrDate = System.currentTimeMillis() - 5*3600000;
        Date dateTime = new Date(pcrDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(dateTime);
    }

    /**
     * 返回当前时间：yyyy-MM-dd
     */
    public static String getPCRDateOfYesterdayString() {
        long pcrDate = System.currentTimeMillis() - 29*3600000;
        Date dateTime = new Date(pcrDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(dateTime);
    }

    public static Date getAfterMonthOfDay() {
        long time = System.currentTimeMillis() + 30 * 86400000L;
        return new Date(time);
    }

}
