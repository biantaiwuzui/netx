package com.netx.shopping.vo;

import java.math.BigDecimal;

public class ProductOrderItemVo {

    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品图片
     */
    private String productImagesUrl;
    /**
     * 规格名称
     */
    private String speName;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer quanty;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImagesUrl() {
        return productImagesUrl;
    }

    public void setProductImagesUrl(String productImagesUrl) {
        this.productImagesUrl = productImagesUrl;
    }

    public String getSpeName() {
        return speName;
    }

    public void setSpeName(String speName) {
        this.speName = speName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuanty() {
        return quanty;
    }

    public void setQuanty(Integer quanty) {
        this.quanty = quanty;
    }
}
