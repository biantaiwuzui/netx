package com.netx.shopping.vo;

import java.math.BigDecimal;
import java.util.List;

public class GetProductListVo {

    /**
     * 商品id
     */
    private String id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * SFM价
     */
    private BigDecimal price;

    /**
     * 专柜价
     */
    private BigDecimal marketPrice;
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
    private String merchantId;
    /**
     * 商家名称
     */
    private String merchantName;

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
     * 是否配送
     */
    private Boolean isDelivery;
    /**
     * 是否支持退换
     */
    private Boolean isReturn;
    /**
     * 商品状态
     * 1.上架
     * 2.下架
     */
    private Integer onlineStatus;

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
     * 配送方式
     1：支持配送
     2：不提供配送，仅限现场消费
     3：提供外卖配送
     */
//    private Integer deliveryWay;
    private List<AddDeliveryWayRequestDto> deliveryWayList;


    public List<AddDeliveryWayRequestDto> getDeliveryWayList() {
        return deliveryWayList;
    }

    public void setDeliveryWayList(List<AddDeliveryWayRequestDto> deliveryWayList) {
        this.deliveryWayList = deliveryWayList;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
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

//    public Integer getDeliveryWay() {
//        return deliveryWay;
//    }
//
//    public void setDeliveryWay(Integer deliveryWay) {
//        this.deliveryWay = deliveryWay;
//    }
}
