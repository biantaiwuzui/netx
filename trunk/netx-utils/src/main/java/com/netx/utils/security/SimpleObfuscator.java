package com.netx.utils.security;

import com.netx.utils.sign.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 *
 * @author Runshine
 * @since 2016-1-12
 * @version 1.0.0
 *
 */
public class SimpleObfuscator {
    private static final char[] l1code;
    private static final char[] l2code;

    static {
        //l1code与l2code中的字符可任意定义，但不要重合，且各自长度应大于10。

        l1code = new char[40];
        for(int i = 0, j = 10; i < l1code.length; i++, j++) {
            l1code[i] = (char)j;
        }

        l2code = new char[30];
        for(int i = 0, j = l1code[l1code.length - 1] + 1; i < l2code.length; i++, j++) {
            l2code[i] = (char)j;
        }
    }

    public static String encode(long l1, long l2) {
        if(l1 < 0 || l2 < 0) throw new RuntimeException("目前不支持对负数的混淆");

        String s1 = l2s(l1, l1code);
        String s2 = l2s(l2, l2code);
        String orderShuffle = orderShuffle(s1, s2);
        try {
            return Base64.encode(orderShuffle.getBytes("UTF-8"));
        } catch(UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String l2s(long l, char[] lcode) {
        StringBuilder stringBuilder = new StringBuilder(42);
        while(l != 0) {
            int t = (int)(l % 10);
            int index = 0;
            do {
                index = t + randomFactor(lcode.length) * 10;
            } while(index >= lcode.length);
            stringBuilder.insert(0, lcode[index]);
            l = l / 10;
        }
        return stringBuilder.toString();
    }

    private static long s2l(String s, char[] lcode) {
        if(s == null || s.length() == 0) return 0;
        long l = 0;
        for(int i = 0; i < s.length(); i++) {
            char charAt = s.charAt(i);
            int j = 0;
            for(; j < lcode.length && lcode[j] != charAt; j++);
            if(j >= lcode.length) {
                throw new RuntimeException("字符串与混淆集合不符");
            } else if(j >= 10) {
                j = j % 10;
            }
            l = l * 10 + j;
        }
        return l;
    }

    private static String orderShuffle(String s1, String s2) {
        int s1length = s1.length();
        int s2length = s2.length();
        int s1Pointer = 0;
        int s2Pointer = 0;
        StringBuilder stringBuilder = new StringBuilder(s1length + s2length);
        Random r = new Random();
        for(; s1Pointer < s1length && s2Pointer < s2length;) {
            if(r.nextBoolean()) {
                stringBuilder.append(s1.charAt(s1Pointer));
                s1Pointer++;
            } else {
                stringBuilder.append(s2.charAt(s2Pointer));
                s2Pointer++;
            }
        }
        if(s1Pointer >= s1length && s2Pointer < s2length) {
            stringBuilder.append(s2.substring(s2Pointer));
        } else if(s2Pointer >= s2length && s1Pointer < s1length) {
            stringBuilder.append(s1.substring(s1Pointer));
        }
        return stringBuilder.toString();
    }

    private static int randomFactor(int codeLength) {
        int y = codeLength % 10;
        int x = (codeLength / 10) + (y == 0 ? 0 : 1);
        return RandomUtils.nextInt(0, x);
    }

    private static String[] orderShuffleDecompose(String s) {
        StringBuilder l1codeString = new StringBuilder(s.length());
        StringBuilder l2codeString = new StringBuilder(s.length());

        for(int i = 0; i < s.length(); i++) {
            char charAt = s.charAt(i);
            if(ArrayUtils.contains(l1code, charAt)) {
                l1codeString.append(charAt);
            } else if((ArrayUtils.contains(l2code, charAt))) {
                l2codeString.append(charAt);
            }
        }

        return new String[]{l1codeString.toString(), l2codeString.toString()};
    }

    public static long[] decode(String s) {
        try {
            if(s == null || s.length() == 0) return new long[]{0, 0};
            String ds = new String(Base64.decode(s), "UTF-8");
            String[] orderShuffleDecompose = orderShuffleDecompose(ds);
            return new long[]{s2l(orderShuffleDecompose[0], l1code), s2l(orderShuffleDecompose[1], l2code)};
        } catch(UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
