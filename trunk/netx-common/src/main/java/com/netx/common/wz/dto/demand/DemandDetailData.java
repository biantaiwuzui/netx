package com.netx.common.wz.dto.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 需求详情显示信息
 */
public class DemandDetailData {
    private String id;
    /**
     * 经度 纬度
     */
    private BigDecimal lon;
    private BigDecimal lat;
    private String userId;
    /**
     * 主题
     */
    private String title;
    /**
     * 需求分类：
     1：技能
     2：才艺
     3：知识
     4：资源
     */
    private Integer demandType;
    /**
     * 标签，逗号分隔
     */
    private List<String> demandLabels;
    /**
     * 是否长期有效
     */
    private Boolean isOpenEnded;
    /**
     * 开始时间
     */
    private Date startAt;
    /**
     * 结束时间
     */
    private Date endAt;
    /**
     * 时间要求：只有大概的要求，如：50天内、仅限周末等
     */
    private String about;
    /**
     * 需求人数
     */
    private Integer amount;
    private String unit;
    /**
     * 报名对象：
     1：不限制。
     2：仅限女性报名
     3：仅限男性报名
     4：仅允许我的好友报名
     5：仅限指定人员报名
     */
    private Integer obj;
    /**
     * 指定报名对象列表，逗号分隔
     */
    private List<String> objectList;
    /**
     * 地址：
     这里商家地址，订单之类的都是发布需求时的，在生成订单时还会生成实际的。
     */
    private String address;

    /**
     * 描述
     */
    private String description;
    /**
     * 活动图片
     */
    private String imagesUrl;
    /**
     * 详情图片
     */
    private String merchantId;
    /**
     * 订单列表
     */
    private String orderIds;
    /**
     * 订单消费
     */
    private BigDecimal orderPrice;
    /**
     * 如发生消费，是否由我全额承担
     */
    private Boolean isPickUp;
    /**
     * 报酬，根据isEachWage判断是总报酬还是单位报酬
     */
    private BigDecimal wage;
    /**
     * 是否为单位报酬，即：以上报酬是否为每个入选者的单位报酬
     */
    private Boolean isEachWage;
    /**
     * 已经托管的保证金
     */
    private BigDecimal bail;
    /**
     * 状态：
     1：已发布
     2：已取消
     3：已关闭
     */
    private Integer status;
    /**
     * 是否支付（托管）
     */
    private Boolean isPay;
    /**
     * 距离
     */
    private Double distance;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 活动发布者的年龄
     */
    private Integer age;

    /**
     * 支持网信
     */
    private Boolean isHoldcredit;

    /**
     * 报名人数
     */
    private Integer registersCount;

    /**
     * 入选人数
     */
    private Integer registerSuccessCount;


    public Integer getRegisterSuccessCount() {
        return registerSuccessCount;
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

    public void setRegisterSuccessCount(Integer registerSuccessCount) {
        this.registerSuccessCount = registerSuccessCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDemandType() {
        return demandType;
    }

    public void setDemandType(Integer demandType) {
        this.demandType = demandType;
    }

    public List<String> getDemandLabels() {
        return demandLabels;
    }

    public void setDemandLabels(List<String> demandLabels) {
        this.demandLabels = demandLabels;
    }

    public Boolean getOpenEnded() {
        return isOpenEnded;
    }

    public void setOpenEnded(Boolean openEnded) {
        isOpenEnded = openEnded;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public List<String> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<String> objectList) {
        this.objectList = objectList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Boolean getPickUp() {
        return isPickUp;
    }

    public void setPickUp(Boolean pickUp) {
        isPickUp = pickUp;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    public Boolean getEachWage() {
        return isEachWage;
    }

    public void setEachWage(Boolean eachWage) {
        isEachWage = eachWage;
    }

    public BigDecimal getBail() {
        return bail;
    }

    public void setBail(BigDecimal bail) {
        this.bail = bail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getHoldcredit() {
        return isHoldcredit;
    }

    public void setHoldcredit(Boolean holdcredit) {
        isHoldcredit = holdcredit;
    }

    public Integer getRegistersCount() {
        return registersCount;
    }

    public void setRegistersCount(Integer registersCount) {
        this.registersCount = registersCount;
    }
}
