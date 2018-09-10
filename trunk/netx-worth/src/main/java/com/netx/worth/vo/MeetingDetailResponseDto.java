package com.netx.worth.vo;

import com.netx.utils.money.Money;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MeetingDetailResponseDto {
    /**
     * 费用不足时：
     1：由我补足差额，活动正常进行
     2：活动自动取消，报名费用全部返回
     */
    private Integer feeNotEnough;
    /**
     *  活动主题
     */
    private String title;
    /**
     *  活动地址
     */
    private String address;
    /**
     * 海报图片
     */
    private String merchantId;
    /**
     * 活动图片
     */
    private String meetingImagesUrl;
    /**
     *  与活动的距离 
     */
    private double distance;
    /**
     *  已报名人数
     */
    private int regCount;
    /**
     * 活动类型：
     1：活动，即1对1
     2：聚合，即多对多
     3：纯线上活动
     4：不发生消费的线下活动
     */
    private Integer meetingType;
    /**
     *  报名费用
     */
    private BigDecimal amount;
    /**
     * 报名总费用
     */
    private BigDecimal allRegisterAmount; 
    /**
     * 活动人数下限
     */
    private int floor;
    /**
     *  活动人数上限
     */
    private int ceil;
    /**
     * 活动时间
     */
    private Date startedAt;
    /**
     * 活动结束时间
     */

    private Date endAt;
    /**
     * 活动描述
     */
    private String description;
    /**
     * 消费订单
     * @return
     */
    private String orderIds;

    /**
     * 活动点击量
     * @return
     */
    private int ClickQuantity;
    /**
     * 活动标签，逗号分隔
     * @return
     */
    private String meetingLabel;
    /**
     * 报名对象
     * @return
     */
    private Integer obj;

    /**
     * userid
     * @return
     */
    private String userId;
    
    @ApiModelProperty(value = "报名状态")
    private Integer registerStatus;
    
    @ApiModelProperty(value = "活动状态")
    private Integer status;
    
    @ApiModelProperty(value = "报名者验证状态")
    private Integer isValidation;
    
    @ApiModelProperty(value = "是否主要发起人")
    private Boolean isDefault;
    
    @ApiModelProperty(value = "是否确认细节")
    private Boolean isConfirm;
    
    @ApiModelProperty(value = "是否支付")
    private Boolean isPay;
    
    @ApiModelProperty(value = "发起人校验状态")
    private Integer sendValidation;

    @ApiModelProperty("用户网号")
    private String userNumber;
    
    @ApiModelProperty(value = "是否评论")
    private Boolean comments;
    
    @ApiModelProperty(value = "是否匿名")
    private Boolean isAnonymity;
    
    @ApiModelProperty(value = "报名截止时间")
    private Date regStopAt;

    @ApiModelProperty(value = "群聊组Id")
    private Long groupId;

    @ApiModelProperty(value = "活动实时状态")
    private String statusDescription;
    
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    
    @ApiModelProperty(value = "维度")
    private BigDecimal lat;
    
    @ApiModelProperty(value = "联合发起人")
    private List<String> sendList;
    
    @ApiModelProperty(value = "订单消费")
    private BigDecimal orderPrice;


    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Long orderPrice) {
        if(orderPrice !=null){
            this.orderPrice = Money.CentToYuan(orderPrice).getAmount() ; 
        }
    }

    public List<String> getSendList() {
        return sendList;
    }

    public void setSendList(List<String> sendList) {
        this.sendList = sendList;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getRegStopAt() {
        return regStopAt;
    }

    public void setRegStopAt(Date regStopAt) {
        this.regStopAt = regStopAt;
    }

    public Boolean getComments() {
        return comments;
    }

    public void setComments(Boolean comments) {
        this.comments = comments;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getIsValidation() {
        return isValidation;
    }

    public void setIsValidation(Integer isValidation) {
        this.isValidation = isValidation;
    }

    public Integer getSendValidation() {
        return sendValidation;
    }

    public void setSendValidation(Integer sendValidation) {
        this.sendValidation = sendValidation;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public String getMeetingLabel() {
        return meetingLabel;
    }

    public void setMeetingLabel(String meetingLabel) {
        this.meetingLabel = meetingLabel;
    }

    public int getClickQuantity() {
        return ClickQuantity;
    }

    public void setClickQuantity(int clickQuantity) {
        ClickQuantity = clickQuantity;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRegCount() {
        return regCount;
    }

    public void setRegCount(int regCount) {
        this.regCount = regCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
            if(amount!=null){
                this.amount = Money.CentToYuan(amount).getAmount();
            }
    }

    public BigDecimal getAllRegisterAmount() {
        return allRegisterAmount;
    }

    public void setAllRegisterAmount(BigDecimal allRegisterAmount) {
        this.allRegisterAmount = allRegisterAmount;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCeil() {
        return ceil;
    }

    public void setCeil(int ceil) {
        this.ceil = ceil;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMeetingImagesUrl() {
        return meetingImagesUrl;
    }

    public void setMeetingImagesUrl(String meetingImagesUrl) {
        this.meetingImagesUrl = meetingImagesUrl;
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

    public Integer getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(Integer registerStatus) {
        this.registerStatus = registerStatus;
    }


    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }


    @Override
    public String toString() {
        return "MeetingDetailResponseDto{" +
                "feeNotEnough=" + feeNotEnough +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", meetingImagesUrl='" + meetingImagesUrl + '\'' +
                ", distance=" + distance +
                ", regCount=" + regCount +
                ", meetingType=" + meetingType +
                ", amount=" + amount +
                ", allRegisterAmount=" + allRegisterAmount +
                ", floor=" + floor +
                ", ceil=" + ceil +
                ", startedAt=" + startedAt +
                ", endAt=" + endAt +
                ", description='" + description + '\'' +
                ", orderIds='" + orderIds + '\'' +
                ", ClickQuantity=" + ClickQuantity +
                ", meetingLabel='" + meetingLabel + '\'' +
                ", obj=" + obj +
                ", userId='" + userId + '\'' +
                ", registerStatus=" + registerStatus +
                ", status=" + status +
                ", isValidation=" + isValidation +
                ", isDefault=" + isDefault +
                ", isConfirm=" + isConfirm +
                ", isPay=" + isPay +
//                ", sendValidation=" + sendValidation +
                ", userNumber='" + userNumber + '\'' +
                ", comments=" + comments +
                ", isAnonymity=" + isAnonymity +
                ", regStopAt=" + regStopAt +
                ", groupId=" + groupId +
                ", statusDescription='" + statusDescription + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", sendList=" + sendList +
                '}';
    }
}
