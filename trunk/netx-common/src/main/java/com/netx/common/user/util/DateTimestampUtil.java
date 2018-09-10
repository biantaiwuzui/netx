package com.netx.common.user.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 操作日期与时间戳工具
 * @author 李卓
 */
public class DateTimestampUtil {
    private static Logger logger = LoggerFactory.getLogger(DateTimestampUtil.class);

    //一天的时间戳差，公式：1000毫秒 * 60秒 * 60分钟 * 24小时
    public static final Long TIME_DIFFERENCE = 1000L * 60 * 60 * 24;

    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";
    private static final String CRON_MONTH_FORMAT = "ss mm HH dd * ? *";
    private static final String CRON_DAY_FORMAT = "ss mm HH * * ? *";
    private static final String CRON_HOUR_FORMAT="ss mm * * * ? *";
    private static final String CRON_MIN_FORMAT="ss * * * * ? *";

    /***
     * 指定日期转为对应的cron的表达式
     * @param date 时间
     * @return  cron类型的日期
     */
    public static String getCron(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    public static String getCronByMonth(Date date,Integer month){
        date = addMonthStartOrEndOfDate(date,0,month);
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_MONTH_FORMAT);
        return sdf.format(date);
    }

    public static String getCronByQuarter(Date date,Integer month){
        date = addMonthStartOrEndOfDate(date,0,month);
        int dateMonth = date.getMonth()%4;
        if(dateMonth==0){
            dateMonth+=4;
        }
        String format = "ss mm HH dd "+dateMonth+"/3"+month+" ? *";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getCronByDay(Date date,Integer day){
        date = addDayStartOrEndOfDate(date,0,day);
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DAY_FORMAT);
        return sdf.format(date);
    }

    private static String getCronByField(Date date,int field,int value,String format){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(field,value);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

    public static String getCronByHour(Date date,Integer hour){
        return getCronByField(date,Calendar.HOUR,hour,CRON_HOUR_FORMAT);
    }

    public static String getCronByMin(Date date,Integer min){
        return getCronByField(date,Calendar.MINUTE,min,CRON_MIN_FORMAT);
    }

    /**
     * 获取某天的开始或结束时间戳
     * @param timestamp 某天的某个时刻的时间戳
     * @param type 获取类型，1：开始时间戳  2：结束时间戳
     * @return
     */
    public static Long getStartOrEndOfTimestamp(Long timestamp, Integer type){
        try {
            Date date = getDateByTimestamp(timestamp);
            return getStartOrEndOfDate(date, type).getTime();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取本月开始时间或结束时间
     * @param date 日期
     * @param month 大于等于0，月份自增，小于0，月份自减
     * @param begin 是否为1号
     * @param type 0:同一时刻   1:开始日期   2：结束日期
     * @return
     */
    public static Date getStartOrEndOfMonthDate(Date date,Integer month,Boolean begin,Integer type){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, begin?1:calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendarToDateGetStartOrEnd(calendar,type);
    }

    public static Date stringToDate(String time,String format){
        SimpleDateFormat sdf= new SimpleDateFormat(format);
        try {
            return sdf.parse(time);
        }catch (Exception e){
            logger.error("格式错误");
        }
        return null;
    }

    /**
     * 获取某天的开始日期或结束日期
     * @param date 某天的某个时刻（日期类型）
     * @param type 获取类型，0:同一时刻   1:开始日期   2：结束日期
     * @return
     */
    public static Date getStartOrEndOfDate(Date date, Integer type){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
       return calendarToDateGetStartOrEnd(calendar,type);
    }

    private static Date calendarToDateGetStartOrEnd(Calendar calendar,Integer type){
        if(type == 1) {//获取开始日期
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }else if(type == 2) {//获取结束日期
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            calendar.set(Calendar.MILLISECOND,999);
        }/*else{
            throw new RuntimeException("type 类型错误，1：开始日期，2：结束日期");
        }*/
        return calendar.getTime();
    }

    /**
     * 获取某天自增或自减对应的月数的开始日期或结束日期
     * @param date 某天的某个时刻（日期类型）
     * @param type 获取类型，0:同一时刻   1:开始日期   2：结束日期
     * @param month 大于等于0，日期自增，小于0，日期自减
     * @return
     */
    public static Date addMonthStartOrEndOfDate(Date date,Integer type,Integer month){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendarToDateGetStartOrEnd(calendar,type);
    }


    /**
     * 获取某天自增或自减对应的天数的开始日期或结束日期
     * @param date 某天的某个时刻（日期类型）
     * @param type 获取类型，1:开始日期   2：结束日期
     * @param day 大于等于0，日期自增，小于0，日期自减
     * @return
     */
    public static Date addDayStartOrEndOfDate(Date date,Integer type,Integer day){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendarToDateGetStartOrEnd(calendar,type);
    }

    /**
     * 获取某天自增或自减对应的周数的开始日期或结束日期
     * @param date 某天的某个时刻（日期类型）
     * @param type 获取类型，1:开始日期   2：结束日期
     * @param week 大于等于0，日期自增，小于0，日期自减
     * @return
     */
    public static Date addWeekStartOrEndOfDate(Date date,Integer type,Integer week){
        return addDayStartOrEndOfDate(date,type,week*7);
    }

    /**
     * 获取某天自增或自减对应的年数的开始日期或结束日期
     * @param date 某天的某个时刻（日期类型）
     * @param type 获取类型，0:同一时刻   1:开始日期   2：结束日期
     * @param year 大于等于0，日期自增，小于0，日期自减
     * @return
     */
    public static Date addYearStartOrEndOfDate(Date date,Integer type,Integer year){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,year);
        return calendarToDateGetStartOrEnd(calendar,type);
    }

    /**
     * 获取某天自增或自减对应的年数
     * @param date 某天的某个时刻（日期类型）
     * @param year 大于等于0，日期自增，小于0，日期自减
     * @return
     */
    public static Date addYearByDate(Date date,Integer year){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,year);
        return calendar.getTime();
    }

    /**
     * 获取某天自增或自减对应的日期数的开始日期或结束日期
     * 注意：先计算年数、再月数、最后是天数
     * @param date 某天的某个时刻（日期类型）
     * @param type 获取类型，1:开始日期   2：结束日期
     * @param day 大于等于0，日期自增，小于0，日期自减
     * @param month 大于等于0，日期自增，小于0，日期自减
     * @param year 大于等于0，日期自增，小于0，日期自减
     * @return
     */
    public static Date addDateStartOrEndOfDate(Date date,Integer type,Integer day,Integer month,Integer year){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,year);
        calendar.add(Calendar.MONTH,month);
        calendar.add(Calendar.DATE,day);
        return calendarToDateGetStartOrEnd(calendar,type);
    }



    /**
     * 时间戳转换为日期
     * @param timestamp
     * @return
     */
    public static Date getDateByTimestamp(Long timestamp){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = format.format(timestamp);
            Date date = format.parse(str);
            return date;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 时间戳转换为String型日期
     * @param timestamp
     * @return
     */
    public static String getDateByTimestamp1(Long timestamp){
        return getDateStrByDate(new Date(timestamp));
    }

    /**
     * Date转换为String型日期
     * @param date
     * @return
     */
    public static String getDateStrByDate(Date date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String getDateStrsByDate(Date date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            return format.format(date);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 日期转为时间戳
     * @param date
     * @return
     */
    public static Long getTimestampByDate(Date date){
        return date.getTime();
    }

    /**
     * 获取当前时间戳的前 n 天或后 n 天的同一时刻的时间戳
     * 例如：当前时间戳为 2017年1月10日12:00，调用方法的参数为（当前时间戳，2，1），可得到：2017年1月8日12:00 的时间戳。
     * @param timestamp 当前时间戳
     * @param days 之前或之后的天数
     * @param type 1：在这个时间戳之前，2：在这个时间戳之后
     * @return 目标时间戳
     */
    public static Long getTimestampForBeforeOrAfterOfDays(Long timestamp, Integer days , Integer type){
        Long timeDifference = TIME_DIFFERENCE * days;
        switch (type){
            case 1:
                return timestamp-timeDifference;
            case 2:
                return timestamp+timeDifference;
        }
        return timestamp;
    }

    /**
     * 根据String型Date返回时间戳（有时间，如2017-1-1 22:00:00）
     */
    public static Long getTimestampByStringDate(String time){
        Date date=new Date();
        try {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date=format.parse(time);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return date.getTime();
    }

    /**
     * 根据String型Date返回时间戳（无时间，如2017-1-1）
     */
    public static Long getTimestampByStringSimpleDate(String time){
        Date date=new Date();
        try {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
            date=format.parse(time);
        } catch (ParseException e) {
            logger.info(e.getMessage());
        }
        return date.getTime();
    }

    /**
     * 获取当前时分
     * @param time
     * @return
     */
    public static String getNowByHM(Long time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String str = format.format(new Date().getTime());
            return str;
        } catch (Exception e) {
        }
        return null;
    }
    /**
     * 根据时分获取时间戳
     */
    public static Long getTimestampByHM(String time){
        String[] list = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(list[1]));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取一个时间戳的月开始时间或者是结束的时间
     * @param time  时间戳
     * @param type  1.开始时间      2.结束时间
     * @return
     */
    public static Date getTimestampOfStartOrEndDate(Long time,int type){
        Date date=new Date(time);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        if(type==1) {
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }else{
            calendar.add(Calendar.MONTH,1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }
        return calendar.getTime();
    }
}
