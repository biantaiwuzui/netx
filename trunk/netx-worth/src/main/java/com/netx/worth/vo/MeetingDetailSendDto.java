package com.netx.worth.vo;

import com.netx.utils.money.Money;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class MeetingDetailSendDto {
//    @ApiModelProperty(value = "id")
//    private String id;
    
    @ApiModelProperty(value = "活动id")
    private String meetingId;
    
    @ApiModelProperty(value = "创建活动id")
    private String userId;
    
    @ApiModelProperty(value = "活动创建时间")
    private Date createTime;

    @ApiModelProperty(value = "活动主题")
    private String title;

    @ApiModelProperty(value = "商家Id")
    private String merchantId;

    @ApiModelProperty(value = "活动图片")
    private String meetingImagesUrl;

    @ApiModelProperty(value = "活动距离")
    private double distance;
    
    @ApiModelProperty(value = "活动描述")
    private String description;

    @ApiModelProperty(value = "已报名人数")
    private Integer regCount;
    
    @ApiModelProperty(value = "报名费用")
    private BigDecimal amount;

//    @ApiModelProperty("用户id")
//    private String id;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("等级")
    private Integer lv;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("出生年月日")
    private Date birthday;

    @ApiModelProperty("信用")
    private Integer credit;

    @ApiModelProperty("是否发行网信")
    private Boolean isPublishCredit;
    
    @ApiModelProperty(value = "活动状态")
    private Integer status;
    
    @ApiModelProperty(value = "活动类型")
    private Integer meetingType;

    @ApiModelProperty(value = "活动实时状态")
    private String statusDescription;


    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Boolean getPublishCredit() {
        return isPublishCredit;
    }

    public void setPublishCredit(Boolean publishCredit) {
        isPublishCredit = publishCredit;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(Integer regCount) {
        this.regCount = regCount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        if(amount!=null){
            this.amount = Money.CentToYuan(amount).getAmount();
        }
    }

    @Override
    public String toString() {
        return "MeetingDetailSendDto{" +
                "meetingId='" + meetingId + '\'' +
                ", userId='" + userId + '\'' +
                ", createTime=" + createTime +
                ", title='" + title + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", meetingImagesUrl='" + meetingImagesUrl + '\'' +
                ", distance=" + distance +
                ", description='" + description + '\'' +
                ", regCount=" + regCount +
                ", amount=" + amount +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", lv=" + lv +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", birthday=" + birthday +
                ", credit=" + credit +
                ", isPublishCredit=" + isPublishCredit +
                ", status=" + status +
                ", meetingType=" + meetingType +
                ", statusDescription='" + statusDescription + '\'' +
                '}';
    }
}
