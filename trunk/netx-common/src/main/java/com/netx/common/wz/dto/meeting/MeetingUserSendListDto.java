package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class MeetingUserSendListDto {

    @ApiModelProperty(value = "活动Id")
    private String id;
    
    @ApiModelProperty(value = "主发起人Id")
    private String userId;
    
    @ApiModelProperty(value = "主题")
    private String title;
    /**
     * 活动形式：
     1：活动，即1对1
     2：聚合，即多对多
     3：纯线上活动
     4：不发生消费的线下活动
     */
    private Integer meetingType;
    
    @ApiModelProperty(value = "活动标签，逗号分隔")
    private String meetingLabel;
    
    @ApiModelProperty(value = "活动开始时间")
    private Date startedAt;
    
    @ApiModelProperty(value = "活动结束时间")
    private Date endAt;
   
    @ApiModelProperty(value = "活动截至报名时间")
    private Date regStopAt;
    /**
     * 报名对象：
     1：不限制。
     2：仅限女性报名
     3：仅限男性报名
     4：仅允许我的好友报名
     5：仅限指定人员报名
     */
    private Integer obj;
    
    @ApiModelProperty(value = "指定报名对象列表，逗号分隔")
    private String objList;
    
    @ApiModelProperty(value = "报名费")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "活动地址")
    private String address;
    
    @ApiModelProperty(value = "订单列表")
    private String orderIds;
   
    @ApiModelProperty(value = "活动消费")
    private BigDecimal orderPrice;
    
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "海报图片")
    private String posterImagesUrl;
    
    @ApiModelProperty(value = "活动图片")
    private String meetingImagesUrl;
   
    @ApiModelProperty(value = "入选人数上限")
    private Integer ceil;
    
    @ApiModelProperty(value = "入选人数下限")
    private Integer floor;
    
    @ApiModelProperty(value = "已报名人数")
    private Integer regCount;
    
    @ApiModelProperty(value = "已入选人数")
    private Integer regSuccessCount;
    /**
     * 活动状态：
     0：已发起，报名中
     1：报名截止，已确定入选人
     2：活动取消
     3：活动失败
     4：活动成功
     5：同意开始，分发验证码
     6：无人验证通过，活动失败
     */
    private Integer status;
    /**
     * 费用不足时：
     1：由我补足差额，活动正常进行
     2：活动自动取消，报名费用全部返回
     */
    private Integer feeNotEnough;
    
    @ApiModelProperty(value = "是否确认细节")
    private Boolean isConfirm;
    
//    @ApiModelProperty(value = "版本")
//    private Integer lockVersion;
    
    @ApiModelProperty(value = "总报名费")
    private BigDecimal allRegisterAmount;
    
    @ApiModelProperty(value = "消费差额，需补足的部分：总订单金额-总报名费")
    private BigDecimal balance;
    
    @ApiModelProperty(value = "是否补足")
    private Boolean isBalancePay;
    
    @ApiModelProperty(value = "补足人")
    private String payFrom;
   
    @ApiModelProperty(value = "活动费用")
    private BigDecimal meetingFeePayAmount;
    
    @ApiModelProperty(value = "活动费用支付人")
    private String meetingFeePayFrom;
    
    @ApiModelProperty(value = "活动费用支付方式")
    private String meetingFeePayType;

    @ApiModelProperty(value = "创建者Id")
    private Date createTime;

    @ApiModelProperty(value = "创建者Id")
    private String createUserId;


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

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public String getMeetingLabel() {
        return meetingLabel;
    }

    public void setMeetingLabel(String meetingLabel) {
        this.meetingLabel = meetingLabel;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Date getRegStopAt() {
        return regStopAt;
    }

    public void setRegStopAt(Date regStopAt) {
        this.regStopAt = regStopAt;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public String getObjList() {
        return objList;
    }

    public void setObjList(String objList) {
        this.objList = objList;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterImagesUrl() {
        return posterImagesUrl;
    }

    public void setPosterImagesUrl(String posterImagesUrl) {
        this.posterImagesUrl = posterImagesUrl;
    }

    public String getMeetingImagesUrl() {
        return meetingImagesUrl;
    }

    public void setMeetingImagesUrl(String meetingImagesUrl) {
        this.meetingImagesUrl = meetingImagesUrl;
    }

    public Integer getCeil() {
        return ceil;
    }

    public void setCeil(Integer ceil) {
        this.ceil = ceil;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(Integer regCount) {
        this.regCount = regCount;
    }

    public Integer getRegSuccessCount() {
        return regSuccessCount;
    }

    public void setRegSuccessCount(Integer regSuccessCount) {
        this.regSuccessCount = regSuccessCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFeeNotEnough() {
        return feeNotEnough;
    }

    public void setFeeNotEnough(Integer feeNotEnough) {
        this.feeNotEnough = feeNotEnough;
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public BigDecimal getAllRegisterAmount() {
        return allRegisterAmount;
    }

    public void setAllRegisterAmount(BigDecimal allRegisterAmount) {
        this.allRegisterAmount = allRegisterAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getBalancePay() {
        return isBalancePay;
    }

    public void setBalancePay(Boolean balancePay) {
        isBalancePay = balancePay;
    }

    public String getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(String payFrom) {
        this.payFrom = payFrom;
    }

    public BigDecimal getMeetingFeePayAmount() {
        return meetingFeePayAmount;
    }

    public void setMeetingFeePayAmount(BigDecimal meetingFeePayAmount) {
        this.meetingFeePayAmount = meetingFeePayAmount;
    }

    public String getMeetingFeePayFrom() {
        return meetingFeePayFrom;
    }

    public void setMeetingFeePayFrom(String meetingFeePayFrom) {
        this.meetingFeePayFrom = meetingFeePayFrom;
    }

    public String getMeetingFeePayType() {
        return meetingFeePayType;
    }

    public void setMeetingFeePayType(String meetingFeePayType) {
        this.meetingFeePayType = meetingFeePayType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
