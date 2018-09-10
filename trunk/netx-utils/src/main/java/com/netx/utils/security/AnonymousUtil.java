package com.netx.utils.security;

/**
 *
 * @author Runshine
 * @since 2015-5-25
 * @version 1.0.0
 *
 */
public class AnonymousUtil {
    public static String anonymous(String s, boolean isProcess) {
        if (isProcess) {
            StringBuilder sb = new StringBuilder(s);
            if (sb.length() > 3) {
                return sb.replace(1, sb.length() - 1, "***").toString();
            } else {
                return sb.replace(1, sb.length(), "***").toString();
            }
        }
        return s;
    }

    public static String anonymous(String s) {
        return anonymous(s, true);
    }
}
