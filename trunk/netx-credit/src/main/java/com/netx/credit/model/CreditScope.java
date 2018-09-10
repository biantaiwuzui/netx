package com.netx.credit.model;

import java.io.Serializable;
import java.util.List;

/**
 * 网信可以使用的范围定义
 */
public class CreditScope implements Serializable {

    /**
     * 线下扫描改网信对应的二维码
     */
    private String qrCode;

    /**
     * 是否包括申请者所有的网能
     */
    private boolean includeNetxWorth;

    private boolean includeMerchant;

    private List<String> merchantIds;
    public CreditScope(){
    }

    public CreditScope(String qrCode, boolean includeNetxWorth, boolean includeMerchant, List<String> merchantIds) {
        this.qrCode = qrCode;
        this.includeNetxWorth = includeNetxWorth;
        this.includeMerchant = includeMerchant;
        this.merchantIds = merchantIds;
    }

    public String getQrCode() {
        return qrCode;
    }

    public boolean isIncludeNetxWorth() {
        return includeNetxWorth;
    }

    public boolean isIncludeMerchant() {
        return includeMerchant;
    }

    public List<String> getMerchantIds() {
        return merchantIds;
    }

    @Override
    public String toString() {
        return "CreditScope{" +
                "qrCode='" + qrCode + '\'' +
                ", includeNetxWorth=" + includeNetxWorth +
                ", includeMerchant=" + includeMerchant +
                ", merchantIds=" + merchantIds +
                '}';
    }
}
