package com.netx.utils;

import java.util.Objects;

/**
 * @author Closure.Yang
 * @since 2015/8/25
 */
public class ConditionUtils {

    public static boolean in(Object source, Object... targets) {
        for (Object target : targets) {
            if (Objects.equals(source, target)) return true;
        }
        return false;
    }

    public static boolean notIn(Object source, Object... targets) {
        for (Object target : targets) {
            if (Objects.equals(source, target)) return false;
        }
        return true;
    }
}
