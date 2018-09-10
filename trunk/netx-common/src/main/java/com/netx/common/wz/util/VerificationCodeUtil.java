package com.netx.common.wz.util;

public class VerificationCodeUtil {
    /**
     * 生成4位随机数。Math.random()取值范围是[0,1)
     * 注：中括号表示可以取到，而小括号表示不能取到！
     * @return
     */
    public static int generator() {
        return (int) (Math.random() * 9000 + 1000);
    }
}
