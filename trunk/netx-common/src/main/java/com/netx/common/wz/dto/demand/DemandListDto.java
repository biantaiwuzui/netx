package com.netx.common.wz.dto.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DemandListDto {

    private String id;
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
    private List<String> objList;
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
    private String images;

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

    /**
     * 创建时间
     */
    private Date publishTime;

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
    private Boolean isHoldCredit;

    /**
     * 报名人数
     */
    private Integer count;

    private String worthType = "Demand";

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public List<String> getObjList() {
        return objList;
    }

    public void setObjList(List<String> objList) {
        this.objList = objList;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
    }

    public String getWorthType() {
        return worthType;
    }

    public void setWorthType(String worthType) {
        this.worthType = worthType;
    }
}
