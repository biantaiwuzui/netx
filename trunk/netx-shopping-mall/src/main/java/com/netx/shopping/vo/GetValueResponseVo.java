package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GetValueResponseVo {

    /**
     * 属性值id
     */
    private String valueId;
    /**
     * 属性值名
     */
    private String valueName;

    /**
     * 库存id
     */
    private String skuId;
    /**
     * 此sku实际库存量,每次销售需要减少
     */
    private Integer storageNums;
    /**
     * 此sku当前销量，每次销售需要增加
     */
    private Integer sellNums;
    /**
     * 单笔订单最大能购买的数量,0为不限制
     */
    private Integer tradeMaxNums;

    /**
     * SFM价
     */
    private BigDecimal price;

    /**
     * 专柜价
     */
    private BigDecimal marketPrice;
    
    @ApiModelProperty("商品标价")
    private String unit;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getStorageNums() {
        return storageNums;
    }

    public void setStorageNums(Integer storageNums) {
        this.storageNums = storageNums;
    }

    public Integer getSellNums() {
        return sellNums;
    }

    public void setSellNums(Integer sellNums) {
        this.sellNums = sellNums;
    }

    public Integer getTradeMaxNums() {
        return tradeMaxNums;
    }

    public void setTradeMaxNums(Integer tradeMaxNums) {
        this.tradeMaxNums = tradeMaxNums;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }
}
