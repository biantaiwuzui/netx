package com.netx.utils.money;

import java.math.BigDecimal;

/**
 * 根据汇率、币种和价格转换新币种
 * @author plus
 * @since 11/13/14.
 */
public class ChangeUtil {
    /**
     * 根据原价格，相关汇率转换
     * @param price
     * @param rate
     * @return
     */
    public static long change(long price,String rate){
        Money money = Money.CentToYuan(price);
        return money.multiply(new BigDecimal(rate)).getCent();
    }

    /**
     * 根据原价格，相关汇率转换，价格单位为元
     * @param price
     * @param rate
     * @return
     */
    public static String change(String price,String rate){
        return Money.getMoneyString(change(Money.YuanToCent(price),rate));
    }
}
