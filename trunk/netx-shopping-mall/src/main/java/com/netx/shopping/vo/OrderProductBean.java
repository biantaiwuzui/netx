package com.netx.shopping.vo;

import com.netx.shopping.model.productcenter.Category;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class OrderProductBean {

    @ApiModelProperty("商品id")
    private String productId;
    
    @ApiModelProperty("商品名")
    private String name;
    
    @ApiModelProperty("商品照片")
    private String productPic;
    
    @ApiModelProperty("库存id")
    private String skuId;
    
    @ApiModelProperty("规格")
    private String value;
    
    @ApiModelProperty("原价")
    private BigDecimal price;
    
    @ApiModelProperty("优惠价")
    private BigDecimal marketPrice;
    
    @ApiModelProperty("数量")
    private int quantity;
    
    @ApiModelProperty("配送方式")
    private Integer deliveryWay;
    
    @ApiModelProperty(value = "售后方式")
    private Boolean isReturn;
    
    @ApiModelProperty(value = "商品单价")
    private String unit;
    
    @ApiModelProperty(value = "商品描述")
    private String characteristic;

    @ApiModelProperty(value = "商品一级类目")
    private List<Category> categories;

    @ApiModelProperty(value = "商品二级类目")
    private List<Category> Tages;


    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getTages() {
        return Tages;
    }

    public void setTages(List<Category> tages) {
        Tages = tages;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }
}
