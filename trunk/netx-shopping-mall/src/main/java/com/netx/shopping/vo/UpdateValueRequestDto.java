package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

public class UpdateValueRequestDto {

    @ApiModelProperty("属性值id")
    @NotBlank(message = "属性值id不能为空")
    private String id;

    @ApiModelProperty("属性值名")
    @NotBlank(message = "属性值名不能为空")
    private String name;

    private String skuId;

    private BigDecimal marketPrice;

    private Integer storageNums;
    
    @ApiModelProperty("商品单价")
    @NotBlank(message = "属性值id不能为空")
    private String unit;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getStorageNums() {
        return storageNums;
    }

    public void setStorageNums(Integer storageNums) {
        this.storageNums = storageNums;
    }
}
