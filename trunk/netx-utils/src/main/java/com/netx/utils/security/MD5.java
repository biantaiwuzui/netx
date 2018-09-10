package com.netx.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Runshine
 * @since 2015-5-25
 * @version 1.0.0
 *
 */
public class MD5 {

    public static byte[] hash(byte[] src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SignMD5");
            md.update(src);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("不能生成MD5摘要", e);
        }
    }

    public static byte[] hash(String src, String charSet) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SignMD5");
            md.update(src.getBytes(charSet));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException("不能生成MD5摘要", e);
        }
    }

    public static byte[] hash(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SignMD5");
            md.update(src.getBytes("UTF-8"));
            return md.digest();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("不能生成MD5摘要", e);
        }
    }

    public static String getHashString(byte[] src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SignMD5");
            md.update(src);
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("不能生成MD5摘要", e);
        }
    }

    public static String getHashString(String src, String charSet) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SignMD5");
            md.update(src.getBytes(charSet));
            return toHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException("不能生成MD5摘要", e);
        }
    }

    public static String getHashString(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SignMD5");
            md.update(src.getBytes("UTF-8"));
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("不能生成MD5摘要", e);
        }
    }

    public static String toHex(byte[] buffer) {
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (byte b : buffer) {
            sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(b & 0x0f, 16));
        }
        return sb.toString();
    }
}
