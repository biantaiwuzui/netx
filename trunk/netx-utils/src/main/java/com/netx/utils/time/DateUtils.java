package com.netx.utils.time;

import org.apache.commons.lang3.StringUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
/**
 * 统一归并所有日期有关的东西
 *
 * @author Runshine
 * @since 2015-5-30
 * @version 1.0.0
 *
 */
public class DateUtils {
    public static Date getOriginalUTC() {
        return parseDate("1970-01-01 00:00:00");
    }

    public static String format(TemporalAccessor temporalAccessor, DateTimeFormatterProvider dateTimeFormatterProvider) {
        if (temporalAccessor instanceof Instant) {
            return dateTimeFormatterProvider.dfmForInstant.format(temporalAccessor);
        }
        return dateTimeFormatterProvider.dfm.format(temporalAccessor);
    }

    public static String format(Date date, DateTimeFormatterProvider dateTimeFormatterProvider) {
        return dateTimeFormatterProvider.dfmForInstant.format(date.toInstant());
    }

    public static String format(Date date, DateTimeFormatterProvider dateTimeFormatterProvider, String replaceIfOTC) {
        String s = dateTimeFormatterProvider.dfmForInstant.format(date.toInstant());
        if (StringUtils.startsWith(s, "1970")) {
            return replaceIfOTC;
        }
        return s;
    }

    public static String formatNow(DateTimeFormatterProvider dateTimeFormatterProvider) {
        return dateTimeFormatterProvider.dfm.format(LocalDateTime.now());
    }

    public static Date parseDate(String s, DateTimeFormatterProvider dateTimeFormatterProvider) {

        return Date.from(dateTimeFormatterProvider.parseInstant(s));
    }

    public static Instant parseInstant(String s, DateTimeFormatterProvider dateTimeFormatterProvider) {
        return dateTimeFormatterProvider.parseInstant(s);
    }

    public static LocalDateTime parseLocalDateTime(String s, DateTimeFormatterProvider dateTimeFormatterProvider) {
        return dateTimeFormatterProvider.parseLocalDateTime(s);
    }

    /**
     * 为日期增加秒数
     *
     * @param date
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date addWeek(Date date, int weeks) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, weeks);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int mins) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, mins);
        return cal.getTime();
    }

    public static Date parseDate(String dateStr, DateFormatType dateFormatType) {
        return (Date) doOperation(dateStr, dateFormatType.getValue());
    }

    /** 时间、日期格式化成字符串 */
    public static String formatDate(Date date) {
        if (null == date) {
            return "";
        }
        return (String) formatDate(date, DateFormatType.DATE_FORMAT_STR);
    }

    /** 时间、日期格式化成字符串 */
    public static String formatDate(Date date, DateFormatType dateFormatType) {
        if (null == date) {
            return "";
        }
        return (String) doOperation(date, dateFormatType.getValue());
    }

    public static String format(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    private static Object doOperation(Object object, String formatStr) {
        if (object == null || null == formatStr || "".equals(formatStr)) {
            throw new RuntimeException("参数不能为空");
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            if (object instanceof Date)
                return format.format(object);
            else
                return format.parse(object.toString());
        } catch (Exception e) {
            throw new RuntimeException("操作失败", e);
        }

    }

    public static Date parseDate(String date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DateFormatType.DATE_FORMAT_STR);
    }

    public static String getCurrentDateStr(DateFormatType dateFormatType) {
        Date date = getCurrentDate();
        return (String) doOperation(date, dateFormatType.getValue());
    }

    /** 获取当前系统时间(原始格式) */
    public static Date getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        return date;
    }

    public enum DateFormatType {

        /** yyyy-MM-dd HH:mm:ss */
        DATE_FORMAT_STR("yyyy-MM-dd HH:mm:ss"),

        /** YYYYMMDDHHMMSS */
        DATE_FORMAT_FOREX("YYYYMMDDHHMMSS"),

        /** yyyy-MM-dd HH:mm */
        DATE_MINUTE_FORMAT_STR("yyyy-MM-dd HH:mm"),

        /** MM/dd/yyyy HH:mm:ss */
        DATE_USA_STYLE("MM/dd/yyyy HH:mm:ss"),

        DATE_USA_STYLE2("yyyy/MM/dd HH:mm:ss"),

        /** MM/dd/yyyy */
        DATE_USA_STYLE3("MM/dd/yyyy"),

        DATE_USA_STYLE4("yyyy/MM/dd"),

        /** yyyy年MM月dd日 HH时mm分ss秒 */
        DATE_FORMAT_STR_CHINA("yyyy年MM月dd日 HH时mm分ss秒"),

        /** yyyy年MM月dd日 HH点 */
        DATE_FORMAT_STR_CHINA_HOUR("yyyy年MM月dd日 HH点"),

        /** yy年MM月dd日 HH点 */
        DATE_FORMAT_STR_CHINA_HOUR_YY("yy年MM月dd日 HH点"),

        /** yyyyMMddHHmmss */
        SIMPLE_DATE_TIME_FORMAT_STR("yyyyMMddHHmmss"),

        /** yyyy-MM-dd */
        SIMPLE_DATE_FORMAT_STR("yyyy-MM-dd"),

        /** yyyyMMdd */
        SIMPLE_DATE_FORMAT("yyyyMMdd"),

        /** yy年MM月dd日 */
        SIMPLE_DATE_FORMAT_STR_YY("yy年MM月dd日"),

        /** yyyy年MM月dd日 */
        SIMPLE_DATE_FORMAT_STR_DAY("yyyy年MM月dd日"),

        /** MM月dd日 */
        SIMPLE_DATE_FORMAT_STR_MD("MM月dd日"),

        /** yyyy/MM/dd */
        SIMPLE_DATE_FORMAT_VIRGULE_STR("yyyy/MM/dd"),

        /** HH:mm:ss MM-dd */
        MONTH_DAY_HOUR_MINUTE_SECOND("HH:mm:ss MM-dd"),

        /** HH:mm:ss */
        HOUR_MINUTE_SECOND("HH:mm:ss"),

        /** HH:mm */
        HOUR_MINUTE("HH:mm"),

        ND("day"),

        NH("hour"),

        NM("mins");

        private final String value;

        DateFormatType(String formatStr) {
            this.value = formatStr;
        }

        public String getValue() {
            return value;
        }
    }

    public static String getDistanceTime(Date publishTime) {
        final long MINUTE = 60000;

        final long HOUR = MINUTE * 60;

        final long DAY = HOUR * 24;

        final long WEEK = DAY * 7;
        if (publishTime != null) {
            Date now = new Date();
            long intervalTimeStamp = now.getTime() - publishTime.getTime();
            if (intervalTimeStamp < MINUTE) {
                return "刚刚";
            } else if (intervalTimeStamp < HOUR) {
                return Math.round(intervalTimeStamp / MINUTE) + "分钟前";
            } else if (intervalTimeStamp < DAY) {
                return Math.round(intervalTimeStamp / HOUR) + "小时前";
            } else if (intervalTimeStamp < WEEK) {
                return Math.round(intervalTimeStamp / DAY) + "天之前";
            } else if (intervalTimeStamp <= 30 * DAY) {
                return "1星期之前";
            } else {
                return "1个月之前";
            }
        } else {
            return "刚刚";
        }

    }

    public static int getYearDiff(Date from, Date to) {
        Calendar _f = Calendar.getInstance();
        _f.setTime(from);

        Calendar _t = Calendar.getInstance();
        _t.setTime(to);

        return Math.abs(_t.get(Calendar.YEAR) - _f.get(Calendar.YEAR));
    }

    public static int getDayDiff(Date from, Date to) {
        Calendar _f = Calendar.getInstance();
        _f.setTime(from);

        Calendar _t = Calendar.getInstance();
        _t.setTime(to);

        return Math.abs(_t.get(Calendar.DAY_OF_YEAR) - _f.get(Calendar.DAY_OF_YEAR));
    }

    public static void main(String[] args) {
        System.out.println(getYearDiff(DateUtils.parseDate("1990-10-12 00:00:00"), new Date()));
    }

}
