package com.netx.common.common.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;

/**
 * Create by wongloong on 17-10-5
 */
public class CommonMD5Util {
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    public static double getDistanceValue(Double d){
        String dstr = String.valueOf(d);
        BigDecimal bd =BigDecimal.valueOf(0);
        if(dstr!=null) {
            bd = new BigDecimal(dstr);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            //判断值大于999.99默认为999.99
            if (bd.compareTo(new BigDecimal(999.99)) > 0) {
                bd = new BigDecimal(999.99);
            }
        }
        return bd.doubleValue();
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
}
