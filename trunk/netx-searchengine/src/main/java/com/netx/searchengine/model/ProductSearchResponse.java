package com.netx.searchengine.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ChenQian
 * @since 2018-4-10
 */
public class ProductSearchResponse {

    /**
     * 商品id
     */
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商家id
     */
    private String merchantId;

    /**
     * 商品详情列表
     */
    private List<String> productImagesUrl;

    /**
     * 基础价格
     */
    private Long price;

    /**
     * 市场价
     */
    private Long marketPrice;

    /**
     * 是否配送
     */
    private Boolean isDelivery;

    /**
     * 商品状态
     * 1.上架
     * 2.下架
     * 3.強制下架
     */
    private Boolean onlineStatus;

    /**
     * 访问量
     */
    private Long visitNum;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 商品类目id
     */
    private List<String> categoryIds;

    /**
     * 商品类目名称
     */
    private List<String> categoryNames;

    /**
     * 商品类目级别
     * 0：一级类目
     * 非0：二级类目
     */
    private List<String> categoryParentIds;

    /**
     * 地址-省
     */
    private String provinceCode;

    /**
     * 地址-市
     */
    private String cityCode;

    /**
     * 地址-区/县
     */
    private String areaCode;

    /**
     * 地址-街道/镇
     */
    private String addrCountry;

    /**
     * 地址-经度
     */
    private BigDecimal lon;

    /**
     * 地址-纬度
     */
    private BigDecimal lat;

    /**
     * 距离
     */
    private Double distance;

    /**
     * 商品成交量
     */
    private Long volume;

    /**
     * 商家信用
     */
    private Integer credit;

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

    private Boolean isReturn;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<String> getProductImagesUrl() {
        return productImagesUrl;
    }

    public void setProductImagesUrl(List<String> productImagesUrl) {
        this.productImagesUrl = productImagesUrl;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Boolean getDelivery() {
        return isDelivery;
    }

    public void setDelivery(Boolean delivery) {
        isDelivery = delivery;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Long visitNum) {
        this.visitNum = visitNum;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<String> getCategoryParentIds() {
        return categoryParentIds;
    }

    public void setCategoryParentIds(List<String> categoryParentIds) {
        this.categoryParentIds = categoryParentIds;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddrCountry() {
        return addrCountry;
    }

    public void setAddrCountry(String addrCountry) {
        this.addrCountry = addrCountry;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
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

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }

}

