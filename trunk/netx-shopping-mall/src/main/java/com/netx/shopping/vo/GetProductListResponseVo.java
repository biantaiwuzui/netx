package com.netx.shopping.vo;

import com.netx.shopping.model.product.ProductSpe;

import java.math.BigDecimal;
import java.util.List;

public class GetProductListResponseVo {

    /**
     * 商品id
     */
    private String id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品基础价格
     */
    private BigDecimal price;

    /**
     * 商品优惠价格
     */
    private BigDecimal discountPrice;
    /**
     * 商品一级类目
     */
    private List<String> tages;
    /**
     * 商品二级类目
     */
    private List<String> categories;
    /**
     * 商家id
     */
    private String sellerId;
    /**
     * 商家名称
     */
    private String sellerName;

    /**
     * 商家信用
     */
    private Integer credit;
    /**
     * 商品列表图
     */
    private String productImagesUrl;
    /**
     * 距离
     */
    private double distance;
    /**
     * 商品所在城市
     */
    private String cityCode;

    /**
     * 商品状态
     * 1.上架
     * 2.下架
     */
    private Integer status;

    /**
     * 是否支持网信
     */
    private Boolean isHoldCredit;

    /**
     * 首单红包
     */
    private BigDecimal firstRate;

    /**
     * 缴费状态
     * 0.已缴费
     * 1.待缴费
     * 2.待续费
     */
    private Integer payStatus;
    /**
     * 商品描述
     */
    private String characteristic;

    /**
     * 商品销量
     */
    private Long volume;

    /**
     * 规格
     */
    private List<ProductSpe> speList;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<String> getTages() {
        return tages;
    }

    public void setTages(List<String> tages) {
        this.tages = tages;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getProductImagesUrl() {
        return productImagesUrl;
    }

    public void setProductImagesUrl(String productImagesUrl) {
        this.productImagesUrl = productImagesUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public List<ProductSpe> getSpeList() {
        return speList;
    }

    public void setSpeList(List<ProductSpe> speList) {
        this.speList = speList;
    }
}
