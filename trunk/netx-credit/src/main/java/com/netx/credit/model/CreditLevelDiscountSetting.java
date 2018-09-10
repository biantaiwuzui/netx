package com.netx.credit.model;

import com.netx.credit.model.constants.CreditStageName;

import java.io.Serializable;

/**
 * 网信折扣级别设置
 *
 * @author FRWIN
 * @since 2018-07-13
 */
public class CreditLevelDiscountSetting implements Serializable{
    /**
     * 折扣名称 CSN_TOP 和 CSN_FRIEND 和 CSN_COMMON
     */
    private CreditStageName creditStageName;
    /**
     * 折扣等级 例 :0.93 0.95 0.97
     */
    private Double levelDiscount;

    public CreditStageName getCreditStageName() {
        return creditStageName;
    }

    public Double getLevelDiscount() {
        return levelDiscount;
    }

    public void setCreditStageName(CreditStageName creditStageName) {
        this.creditStageName = creditStageName;
    }

    public void setLevelDiscount(Double levelDiscount) {
        this.levelDiscount = levelDiscount;
    }
}
