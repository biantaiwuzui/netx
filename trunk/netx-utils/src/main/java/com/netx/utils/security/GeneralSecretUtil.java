package com.netx.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public class GeneralSecretUtil {
    private static final String M = "AES";
    private static final String U = "UTF8";
    private static final String B = "!";
    private static final int L = 128;
    private static final byte[] sec;

    static {
        try {
            sec = "NmwqdpU(V*13h8%&MO".getBytes(U);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] decode(String value) {
        try {
            byte[] stringDecoder = stringDecoder2Byte(value);
            Cipher cipher = newCipher(Cipher.DECRYPT_MODE, sec);
            byte[] doFinal = cipher.doFinal(stringDecoder);
            return doFinal;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(byte[] content) {
        try {
            Cipher cipher = newCipher(Cipher.ENCRYPT_MODE, sec);
            byte[] result = cipher.doFinal(content);
            return byteEncoder2String(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Cipher newCipher(int cipherMode, byte[] secBytes) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(M);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(secBytes);
        kgen.init(L, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, M);
        Cipher cipher = Cipher.getInstance(M);// 创建密码器   
        cipher.init(cipherMode, key);
        return cipher;
    }

    private static String byteEncoder2String(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte c : b) {
            stringBuilder.append(c).append(B);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    private static byte[] stringDecoder2Byte(String s) {
        if (StringUtils.isBlank(s)) {
            return new byte[0];
        }

        String[] split = s.split(B);
        byte[] b = new byte[split.length];
        for (int i = 0;i < split.length;i++) {
            b[i] = Byte.parseByte(split[i]);
        }

        return b;
    }
}
