package com.netx.credit.vo;

/**
 * @author lanyingchu
 * @date 2018/8/4 10:53
 */
public class BeforPublishVo {
    /**
     * 商家id
     */
    private String merchantId;
    /**
     * 商家名称
     */
    private String merchantNmae;
    /**
     * 预售上限
     */
    private Integer presaleUpperLimit;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantNmae() {
        return merchantNmae;
    }

    public void setMerchantNmae(String merchantNmae) {
        this.merchantNmae = merchantNmae;
    }

    public Integer getPresaleUpperLimit() {
        return presaleUpperLimit;
    }

    public void setPresaleUpperLimit(Integer presaleUpperLimit) {
        this.presaleUpperLimit = presaleUpperLimit;
    }
}
