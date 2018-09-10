package com.netx.utils.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;

public class BCryptUtil {

    private static final Log LOGGER = LogFactory.getLog(BCryptUtil.class);

    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }

        try {
            return content.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static String encryptPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpsd(password, salt);
    }

    public static boolean check(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        }
        catch (Exception e) {
            LOGGER.error("检查密码时异常(" + password + ", " + hashedPassword + "):", e);
            return false;
        }
    }
}
