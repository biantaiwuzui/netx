package com.netx.utils.checker;


/**
 *
 * @author Runshine
 * @since 2015-9-26
 * @version 1.0.0
 *
 */
public class Assert {
    public static void nullAssert(Object obj, String message) {
        if (obj == null) throw new RuntimeException(message);
    }

    public static void nullAssert(Object obj) {
        if (obj == null) throw new RuntimeException();
    }

    public static void gtZero(int obj) {
        if (obj <= 0) throw new RuntimeException("参数必须大于0");
    }

    public static void gtZero(long obj) {
        if (obj <= 0) throw new RuntimeException("参数必须大于0");
    }
}
