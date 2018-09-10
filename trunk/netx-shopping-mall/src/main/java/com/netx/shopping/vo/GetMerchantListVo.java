package com.netx.shopping.vo;

import java.math.BigDecimal;
import java.util.List;

public class GetMerchantListVo {
    /**
     * 商家id
     */
    private String id;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 商家注册者userId
     */
    private String userId;
    /**
     * 商家注册者昵称
     */
    private String nickname;
    /**
     * 商品二级类目
     */
    private List<String> tages;
    /**
     * 商品一级类目
     */
    private List<String> categories;

    /**
     * 商家信用
     */
    private Integer credit;
    /**
     * 商品列表图
     */
    private String logoImagesUrl;
    /**
     * 距离
     */
    private double distance;
    /**
     * 商品所在城市
     */
    private String cityCode;

    /**
     * 商家状态
     * 1.正常
     * 2.拉黑
     */
    private Integer status;

    /**
     * 商家描述
     */
    private String desc;

    /**
     * 首单红包
     */
    private BigDecimal firstRate;

    /**
     * 支持网值
     */
    private Boolean isHoldCredit;

    /**
     * 缴费状态
     * 0.已缴费
     * 1.待缴费
     * 2.待续费
     */
    private Integer payStatus;

    /**
     * 销量
     */
    private Long volume;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getLogoImagesUrl() {
        return logoImagesUrl;
    }

    public void setLogoImagesUrl(String logoImagesUrl) {
        this.logoImagesUrl = logoImagesUrl;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}
