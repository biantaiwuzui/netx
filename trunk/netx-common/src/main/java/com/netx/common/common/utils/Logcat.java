package com.netx.common.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by wongloong on 17-9-19
 */
public class Logcat {

    public static void error(String msg, Exception e) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Logger logger = LoggerFactory.getLogger(stackTrace[2].getClassName());
        logger.error(msg, e);
    }

    public static void info(String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Logger logger = LoggerFactory.getLogger(stackTrace[2].getClassName());
        logger.info(msg);
    }

    public static void info(String msg, Object... args) {
        if (null != args) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("#" + i, args[i].toString());
            }
        }
        info(msg);
    }

    public static void warn(String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Logger logger = LoggerFactory.getLogger(stackTrace[2].getClassName());
        logger.warn(msg);
    }

    public static void warn(String msg, Object... args) {
        if (null != args) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("#" + i, args[i].toString());
            }
        }
        warn(msg);
    }
    public static void error(String msg, Object... args) {
        if (null != args) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("#" + i, args[i].toString());
            }
        }
        warn(msg);
    }
}
