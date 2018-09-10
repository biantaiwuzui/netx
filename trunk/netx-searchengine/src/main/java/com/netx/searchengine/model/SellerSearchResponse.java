package com.netx.searchengine.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ChenQian
 * @since 2018-4-10
 */
public class SellerSearchResponse {

    /**
     * 商家id
     */
    private String id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商家注册用户昵称
     */
    private String nickname;
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
     * 标识图片
     */
    private List<String> logoImagesUrl;
    /**
     * 创建时间
     */
    private Date createTime;

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
     * 地址-详细地址
     */
    private String addrDetail;

    /**
     * 浏览数量
     */
    private Integer visitNum;

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
     * 商家信用
     */
    private Integer credit;

    /**
     * 商家状态
     * 1.正常
     * 2.拉黑
     */
    private Integer status;

    /**
     * 商品描述
     */
    private String desc;

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

    public List<String> getLogoImagesUrl() {
        return logoImagesUrl;
    }

    public void setLogoImagesUrl(List<String> logoImagesUrl) {
        this.logoImagesUrl = logoImagesUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
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

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}
