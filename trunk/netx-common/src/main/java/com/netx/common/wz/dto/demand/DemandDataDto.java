package com.netx.common.wz.dto.demand;

import java.math.BigDecimal;
import java.util.Date;


public class DemandDataDto {
    /**
     * 申请需求的id
     */
    private String DemandRegisterId;

    /**
     * 状态
     */
    private Integer status;
    /**
     *  demand的id
     */
    private String id;
    /**
     * 发布的用户id
     */
    private String userId;
    /**
     * 主题
     */
    private String title;
    /**
     * 时间单位
     */
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
     * 地址：
     这里商家地址，订单之类的都是发布需求时的，在生成订单时还会生成实际的。
     */
    private String address;
    /**
     * 活动图片
     */
    private String imagesUrl;
    /**
     * 详情图片
     */
    private String detailsImagesUrl;
    /**
     * 订单列表
     */
    private String orderIds;
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
     * 是否支付（托管）
     */
    private Boolean isPay;
    /**
     * 距离
     */
    private Double distance;
    /**
     * 发布者ID
     */
    private String publisherId;

    /**
     * 发布者昵称
     */
    private String nickname;

    /**
     * 发布者性别
     */
    private String sex;

    /**
     * 发布者生日
     */
    private Date birthday;

    /**
     * 发布者手机
     */
    private String mobile;

    /**
     * 发布者总积分
     */
    private BigDecimal score;

    /**
     * 发布者信用
     */
    private Integer credit;
//
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发布者等级
     */
    private Integer lv;

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

    public String getDemandRegisterId() {
        return DemandRegisterId;
    }

    public void setDemandRegisterId(String demandRegisterId) {
        DemandRegisterId = demandRegisterId;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getDetailsImagesUrl() {
        return detailsImagesUrl;
    }

    public void setDetailsImagesUrl(String detailsImagesUrl) {
        this.detailsImagesUrl = detailsImagesUrl;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
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

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
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
