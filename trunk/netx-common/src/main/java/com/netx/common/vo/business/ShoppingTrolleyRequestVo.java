package com.netx.common.vo.business;

import java.math.BigDecimal;

public class ShoppingTrolleyRequestVo {

    /**
     * 商家id
     */
    private String sellerId;
    /**
     * 商家名称
     */
    private String sellerName;
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品图片
     */
    private String productImagesUrl;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 商品规格id
     */
    private String productSpeId;
    /**
     * 商品规格名称
     */
    private String productSpeName;

    /**
     * 订单id
     */
    private String orderId;

    private Integer deliveryWay;

    private Boolean isDelivery;

    private Boolean isReturn;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductSpeId() {
        return productSpeId;
    }

    public void setProductSpeId(String productSpeId) {
        this.productSpeId = productSpeId;
    }

    public String getProductSpeName() {
        return productSpeName;
    }

    public void setProductSpeName(String productSpeName) {
        this.productSpeName = productSpeName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public Boolean getDelivery() {
        return isDelivery;
    }

    public void setDelivery(Boolean delivery) {
        isDelivery = delivery;
    }

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }
}
