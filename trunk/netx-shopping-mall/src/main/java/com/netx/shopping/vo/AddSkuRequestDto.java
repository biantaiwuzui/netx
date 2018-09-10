package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class AddSkuRequestDto {

    @ApiModelProperty("库存id，发布不填")
    private String id;

    @ApiModelProperty("对应的商品id，发布不填")
    private String productId;

    @ApiModelProperty("实际库存量")
    //@NotNull(message = "实际库存量不能为空")
    private Integer storageNums;

    @ApiModelProperty("单笔订单最大能购买的数量,0为不限制")
    private Integer tradeMaxNums = 0;

    @ApiModelProperty("专柜价")
    //@NotNull(message = "专柜价不能为空")
    private BigDecimal marketPrice;

    @ApiModelProperty("SFM价")
    //@NotNull(message = "SFM价不能为空")
    private BigDecimal price;

    @ApiModelProperty("条形码，可不填")
    private String skuBarCode;

    @ApiModelProperty("为1时作为默认sku")
    private Integer defaultSku = 1;

    @ApiModelProperty("商品具体规格")
    //@NotNull(message = "商品具体规格不能为空")
    private List<AddProductSkuSpecRequestDto> addProductSkuSpecDtoList;
    
    @ApiModelProperty("商品标价")
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getStorageNums() {
        return storageNums;
    }

    public void setStorageNums(Integer storageNums) {
        this.storageNums = storageNums;
    }

    public Integer getTradeMaxNums() {
        return tradeMaxNums;
    }

    public void setTradeMaxNums(Integer tradeMaxNums) {
        this.tradeMaxNums = tradeMaxNums;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuBarCode() {
        return skuBarCode;
    }

    public void setSkuBarCode(String skuBarCode) {
        this.skuBarCode = skuBarCode;
    }

    public Integer getDefaultSku() {
        return defaultSku;
    }

    public void setDefaultSku(Integer defaultSku) {
        this.defaultSku = defaultSku;
    }

    public List<AddProductSkuSpecRequestDto> getAddProductSkuSpecDtoList() {
        return addProductSkuSpecDtoList;
    }

    public void setAddProductSkuSpecDtoList(List<AddProductSkuSpecRequestDto> addProductSkuSpecDtoList) {
        this.addProductSkuSpecDtoList = addProductSkuSpecDtoList;
    }
}
