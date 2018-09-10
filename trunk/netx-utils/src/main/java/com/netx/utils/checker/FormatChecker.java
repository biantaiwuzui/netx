package com.netx.utils.checker;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author Closure.Yang
 * @since 2015/11/30
 */
public class FormatChecker {

    public final static String MOBILE_FORMAT = "^1[0-9]{10}$";
    public final static String EMAIL_FORMAT = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    public static boolean checkMobile(String mobile) {
        return checkRegex(MOBILE_FORMAT, mobile);
    }

    public static boolean checkEmail(String email) {
        return checkRegex(EMAIL_FORMAT, email);
    }

    public static boolean checkRegex(String regex, String content) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }
        return Pattern.compile(regex).matcher(content).matches();
    }

}
