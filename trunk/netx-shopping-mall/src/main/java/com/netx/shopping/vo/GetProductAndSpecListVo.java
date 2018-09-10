package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class GetProductAndSpecListVo extends GetProductListVo{

    /**
     * 最低价
     */
    private BigDecimal minPrice;

    /**
     * 最高价
     */
    private BigDecimal maxPrice;
    /**
     * 属性列表
     */
    private List<GetPropertyResponseVo> propertyList;
    
    @ApiModelProperty(value = "商品单价")
    private String unit;

    
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<GetPropertyResponseVo> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<GetPropertyResponseVo> propertyList) {
        this.propertyList = propertyList;
    }
}
