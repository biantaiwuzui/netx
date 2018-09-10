package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class SelectUserMeetingResponseDto {

    @ApiModelProperty("业务id")
    private String id;

    @ApiModelProperty("主发起人ID")
    private String userId;

    @ApiModelProperty("活动id")
    private String meetingId;

    @ApiModelProperty("主题")
    private String title;

    @ApiModelProperty("活动形式：1-1对1;2-多对多;3-纯线上活动;4-不发生消费的线下活动")
    private Integer meetingType;
    /**
     * 活动标签，逗号分隔
     */
    private String meetingLabel;

    @ApiModelProperty("活动开始时间")
    private Long startedAt;

    @ApiModelProperty("活动结束时间")
    private Long endAt;

    @ApiModelProperty("活动截至报名时间")
    private Long regStopAt;

    /**
     * 指定报名对象列表，逗号分隔
     */
    private String objList;

    @ApiModelProperty("报名费")
    private BigDecimal amount;

    @ApiModelProperty("活动地址")
    private String address;

    @ApiModelProperty("订单列表")
    private String orderIds;

    @ApiModelProperty("活动经纬度hash")
    private String geohash;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("活动图片")
    private String pic;

    @ApiModelProperty("报名人数上限")
    private Integer ceil;
    /**
     * 报名人数下限
     */
    @ApiModelProperty("报名人数下限")
    private Integer floor;

    @ApiModelProperty("活动状态：0-已发起，报名中;1-报名截止，已确定入选人;2-活动取消;3-活动失败;4-活动成功;5-同意开始，分发验证码")
    private Integer status;

    @ApiModelProperty("报名人数")
    private Integer regCount;

    @ApiModelProperty("入选人数")
    private Integer regSuccessCount;

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

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
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

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public Long getRegStopAt() {
        return regStopAt;
    }

    public void setRegStopAt(Long regStopAt) {
        this.regStopAt = regStopAt;
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

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
