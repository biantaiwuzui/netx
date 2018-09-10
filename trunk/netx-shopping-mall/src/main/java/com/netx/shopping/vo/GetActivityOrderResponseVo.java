package com.netx.shopping.vo;

import java.math.BigDecimal;
import java.util.List;

public class GetActivityOrderResponseVo {
    /**
     * 订单总额
     */
    private BigDecimal totalPrice;
    /**
     * 商家名字
     */
    private String sellerName;
    /**
     * 商家地址-省
     */
    private String provinceCode;
    /**
     * 商家地址-市
     */
    private String cityCode;
    /**
     * 商家地址-区
     */
    private String areaCode;
    /**
     * 商家地址-乡镇
     */
    private String addrCountry;
    /**
     * 商家地址-详细
     */
    private String addrDetail;
    /**
     * 商家地址-详细-门牌
     */
    private String addrDoorNumber;
    /**
     * 商家照片，多个逗号隔开数量不限
     */
    private String sellerImagesUrl;
    /**
     * 距离
     */
    private double distance;
    /**
     * 是否支持网信
     */
    private Boolean holdCredit;
    /**
     * 商家信用值
     */
    private Integer credit;
    /**
     * 首单红包
     */
    private BigDecimal firstRate;
    /**
     * 商家第一类目名称
     */
    private String categoryName;
    /**
     * 商品列表
     */
    private List<ProductOrderItemVo> list;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
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

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getAddrDoorNumber() {
        return addrDoorNumber;
    }

    public void setAddrDoorNumber(String addrDoorNumber) {
        this.addrDoorNumber = addrDoorNumber;
    }

    public String getSellerImagesUrl() {
        return sellerImagesUrl;
    }

    public void setSellerImagesUrl(String sellerImagesUrl) {
        this.sellerImagesUrl = sellerImagesUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Boolean getHoldCredit() {
        return holdCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        this.holdCredit = holdCredit;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductOrderItemVo> getList() {
        return list;
    }

    public void setList(List<ProductOrderItemVo> list) {
        this.list = list;
    }
}
