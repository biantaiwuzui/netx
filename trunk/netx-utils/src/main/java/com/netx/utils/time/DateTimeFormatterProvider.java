package com.netx.utils.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by CloudZou on 1/10/2018.
 */
public abstract class DateTimeFormatterProvider {
    static final String instantFomat = "1970-01-01T00:00:00.000Z";
    static final String localDateTimeFomat = "1970-01-01T00:00:00.000";
    static final String T = "T";

    final DateTimeFormatter dfm;
    final DateTimeFormatter dfmForInstant;

    private DateTimeFormatterProvider(String pattern) {
        dfm = DateTimeFormatter.ofPattern(pattern);
        dfmForInstant = dfm.withZone(ZoneId.systemDefault());
    }

    abstract Instant parseInstant(String s);

    abstract LocalDateTime parseLocalDateTime(String s);

    /**
     * yyyyMM的DateTimeFormatterProvider
     */
    public static final DateTimeFormatterProvider yyyyMM = new DateTimeFormatterProvider("yyyyMM") {

        @Override
        Instant parseInstant(String s) {
            StringBuilder instantBuilder = new StringBuilder(instantFomat.length());
            instantBuilder.append(instantFomat);
            instantBuilder.replace(0, 4, s.substring(0, 4));
            instantBuilder.replace(5, 7, s.substring(4));
            return Instant.parse(instantBuilder.toString());
        }

        @Override
        LocalDateTime parseLocalDateTime(String s) {
            StringBuilder localDateTimeBuilder = new StringBuilder(localDateTimeFomat.length());
            localDateTimeBuilder.append(DateTimeFormatterProvider.localDateTimeFomat);
            localDateTimeBuilder.replace(0, 4, s.substring(0, 4));
            localDateTimeBuilder.replace(5, 7, s.substring(4));
            return LocalDateTime.parse(localDateTimeBuilder.toString());
        }

    };

    /**
     * 原始的1970-01-01 00:00:00
     */
    public static final DateTimeFormatterProvider originalUTC = new DateTimeFormatterProvider("yyyy-MM-dd HH:mm:ss") {

        @Override
        Instant parseInstant(String s) {
            return Instant.parse(instantFomat);
        }

        @Override
        LocalDateTime parseLocalDateTime(String s) {
            return LocalDateTime.parse(localDateTimeFomat);
        }

    };

    /**
     * yyyyMMddHHmmss的DateTimeFormatterProvider
     */
    public static final DateTimeFormatterProvider yyyyMMddHHmmss = new DateTimeFormatterProvider("yyyyMMddHHmmss") {

        @Override
        Instant parseInstant(String s) {
            StringBuilder instantBuilder = new StringBuilder(instantFomat.length());
            instantBuilder.append(instantFomat);
            instantBuilder.replace(0, 4, s.substring(0, 4));
            instantBuilder.replace(5, 7, s.substring(4, 6));
            instantBuilder.replace(8, 10, s.substring(6, 8));
            instantBuilder.replace(11, 13, s.substring(8, 10));
            instantBuilder.replace(14, 16, s.substring(10, 12));
            instantBuilder.replace(17, 19, s.substring(12));
            return Instant.parse(instantBuilder.toString());
        }

        @Override
        LocalDateTime parseLocalDateTime(String s) {
            StringBuilder localDateTimeBuilder = new StringBuilder(localDateTimeFomat.length());
            localDateTimeBuilder.append(DateTimeFormatterProvider.localDateTimeFomat);
            localDateTimeBuilder.replace(0, 4, s.substring(0, 4));
            localDateTimeBuilder.replace(5, 7, s.substring(4, 6));
            localDateTimeBuilder.replace(8, 10, s.substring(6, 8));
            localDateTimeBuilder.replace(11, 13, s.substring(8, 10));
            localDateTimeBuilder.replace(14, 16, s.substring(10, 12));
            localDateTimeBuilder.replace(17, 19, s.substring(12));
            return LocalDateTime.parse(localDateTimeBuilder.toString());
        }

    };

    /**
     * yyyy-MM-dd HH:mm:ss的Formatter
     */
    public static final DateTimeFormatterProvider yyyy_MM_dd_HH_mm_ss = new DateTimeFormatterProvider("yyyy-MM-dd HH:mm:ss") {

        @Override
        Instant parseInstant(String s) {
            StringBuilder instantBuilder = new StringBuilder(instantFomat.length());
            instantBuilder.append(DateTimeFormatterProvider.instantFomat);
            instantBuilder.replace(0, s.length(), s);
            instantBuilder.replace(10, 11, T);
            return Instant.parse(instantBuilder.toString());
        }

        @Override
        LocalDateTime parseLocalDateTime(String s) {
            return LocalDateTime.parse(s, dfm);
        }

    };

    /**
     * 给VM模板调用
     */
    public static DateTimeFormatterProvider getYyyyMM() {
        return yyyyMM;
    }

    /**
     * 给VM模板调用
     */
    public static DateTimeFormatterProvider getYyyy_MM_dd_HH_mm_ss() {
        return yyyy_MM_dd_HH_mm_ss;
    }

    /**
     * 给VM模板调用
     */
    public static DateTimeFormatterProvider getYyyyMMddHHmmss() {
        return yyyyMMddHHmmss;
    }

    /**
     * 给VM模板调用
     */
    public static DateTimeFormatterProvider getOriginalUTC() {
        return originalUTC;
    }
}
