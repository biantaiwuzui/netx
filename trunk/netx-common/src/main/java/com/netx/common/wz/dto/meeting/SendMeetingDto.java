package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class SendMeetingDto {
    @ApiModelProperty(value = "活动ID")
    private String id;

    @ApiModelProperty(value = "发起人ID")
//    @NotBlank(message = "发起人ID不能为空")
    private String userId;

    @ApiModelProperty(value = "主题")
    @NotBlank(message = "主题不能为空")
    private String title;
    /**
     * 活动类型：
     * 1：1对1
     * 2：多对多
     * 3：纯线上活动
     * 4：不发生消费的线下活动
     */
    @ApiModelProperty(value = "活动类型：1：1对1。2：多对多。3：纯线上活动。4：不发生消费的线下活动")
    @NotNull(message = "活动类型不能为空")
    private Integer meetingType;

    @ApiModelProperty(value = "活动标签，逗号分隔")
    @NotBlank(message = "活动标签不能为空")
    private String meetingLabel;
    /**
     *
     */
    @ApiModelProperty(value = "活动开始时间，UNIX时间戳，精确到毫秒")
    @NotNull(message = "活动开始时间不能为空")
    private Long startedAt;

    @ApiModelProperty(value = "活动结束时间，UNIX时间戳，精确到毫秒")
    @NotNull(message = "活动结束时间不能为空")
    private Long endAt;

    @ApiModelProperty(value = "活动截至报名时间，UNIX时间戳，精确到毫秒")
    @NotNull(message = "活动截至报名时间不能为空")
    private Long regStopAt;
    @ApiModelProperty(value = "报名对象：\n" +
            "     1：不限制。\n" +
            "     2：仅限女性报名\n" +
            "     3：仅限男性报名\n" +
            "     4：仅允许我的好友报名\n" +
            "     5：仅限指定人员报名")
    @NotNull(message = "报名对象不能为空")
    private Integer obj;
    @ApiModelProperty(value = "指定报名对象列表，传ID，逗号分隔")
    private String objList;

    @ApiModelProperty(value = "报名费，0为免费。")
    @NotNull(message = "报名费不能为空")
    @Range(min=0, max=100000,message = "单笔充值金额必须大于0元，小于10万元")
    private BigDecimal amount;

    @ApiModelProperty(value = "活动地址")
    private String address;

    @ApiModelProperty(value = "订单列表")
    private String orderIds;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "简介")
    @NotBlank(message = "简介不能为空")
    private String description;

    @ApiModelProperty(value = "活动图片URL")
    private String pic;

    @ApiModelProperty(value = "改为商家Id")
    private String merchantId;

    @ApiModelProperty(value = "联合发起人列表，逗号分隔")
    private String sendIds;

    @ApiModelProperty(value = "报名人数上限")
    @NotNull(message = "报名人数上限不能为空")
    private Integer ceil;

    @ApiModelProperty(value = "报名人数下限")
    @NotNull(message = "报名人数下限不能为空")
    private Integer floor;

    @ApiModelProperty(value = "费用不足时：\n" +
            "     1：由我补足差额，活动正常进行\n" +
            "     2：活动自动取消，报名费用全部返回")
    @NotNull(message = "费用不足的选项不能为空")
    private Integer feeNotEnough;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSendIds() {
        return sendIds;
    }

    public void setSendIds(String sendIds) {
        this.sendIds = sendIds;
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

    public Integer getFeeNotEnough() {
        return feeNotEnough;
    }

    public void setFeeNotEnough(Integer feeNotEnough) {
        this.feeNotEnough = feeNotEnough;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
