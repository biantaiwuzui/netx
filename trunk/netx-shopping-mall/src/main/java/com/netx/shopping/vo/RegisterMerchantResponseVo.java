package com.netx.shopping.vo;

import java.math.BigDecimal;
import java.util.List;

public class RegisterMerchantResponseVo {

    private String shippingFeeId;

    private BigDecimal shippingFee;

    private List<String> propertIds;

    public String getShippingFeeId() {
        return shippingFeeId;
    }

    public void setShippingFeeId(String shippingFeeId) {
        this.shippingFeeId = shippingFeeId;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public List<String> getPropertIds() {
        return propertIds;
    }

    public void setPropertIds(List<String> propertIds) {
        this.propertIds = propertIds;
    }
}
