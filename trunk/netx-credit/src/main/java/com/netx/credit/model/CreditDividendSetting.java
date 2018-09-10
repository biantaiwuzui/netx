package com.netx.credit.model;

import com.netx.credit.model.constants.CreditDividendCategory;

import java.io.Serializable;

/**
 * 网信分红设置
 */
public class CreditDividendSetting implements Serializable {

    /**
     * 分红类型 CDC_WORTH 和 CDC_MERCHANT
     */
    private CreditDividendCategory creditDividendCategory;

    /**
     * 每一单对应的分红值 例:0.05 0.03
     */
    private Double ratio;

    public CreditDividendCategory getCreditDividendCategory() {
        return creditDividendCategory;
    }

    public Double getRatio() {
        return ratio;
    }

}

