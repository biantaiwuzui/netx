package com.netx.utils.checker;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by spark on 2015/11/12.
 */
public class ParamCheck {
    /**
     * @param exp      条件表达式
     * @param errorMsg exp为true时抛出的异常信息
     */
    public static void throwsIllegalArgumentException(boolean exp, String errorMsg) {
        if (exp) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    /**
     * @param exp      条件表达式
     * @param errorMsg exp为true时抛出的异常信息
     */
    public static void throwsRuntimeException(boolean exp, String errorMsg) {
        if (exp) {
            throw new RuntimeException(errorMsg);
        }
    }

    public static void checkBlank(String val, String errorMsg) {
        throwsIllegalArgumentException(StringUtils.isBlank(val), errorMsg);
    }

    public static <T extends Enum> void checkEnum(Class<T> clz, String name, String errorMsg) {
        T.valueOf(clz, name);
    }

}
