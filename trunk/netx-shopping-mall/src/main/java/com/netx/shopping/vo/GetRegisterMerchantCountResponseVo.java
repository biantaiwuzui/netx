package com.netx.shopping.vo;

public class GetRegisterMerchantCountResponseVo {

    /**
     * 现有注册的商店数
     */
    private Integer nowRegisterMerchant = 0;

    /**
     * 总注册过的商店数
     */
    private Integer sumRegisterMerchant = 0;

    public Integer getNowRegisterMerchant() {
        return nowRegisterMerchant;
    }

    public void setNowRegisterMerchant(Integer nowRegisterMerchant) {
        this.nowRegisterMerchant = nowRegisterMerchant;
    }

    public Integer getSumRegisterMerchant() {
        return sumRegisterMerchant;
    }

    public void setSumRegisterMerchant(Integer sumRegisterMerchant) {
        this.sumRegisterMerchant = sumRegisterMerchant;
    }
}
