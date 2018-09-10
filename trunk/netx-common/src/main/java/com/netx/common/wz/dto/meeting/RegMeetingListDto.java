package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class RegMeetingListDto {
    
    @ApiModelProperty(value = "活动Id")
    private String meetingId;;
    
    @ApiModelProperty(value = "活动主题")
    private String title;
    
    @ApiModelProperty(value = "活动海报")
    private String posterImagesUrl;
    
    @ApiModelProperty(value = "活动图片")
    private String meetingImagesUrl;
    
    @ApiModelProperty(value = "活动距离")
    private Double distance;
    
    @ApiModelProperty(value = "已报名人数")
    private Integer regCount;

    @ApiModelProperty("用户id")
    private String id;

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

    @ApiModelProperty("报名费")
    private BigDecimal fee;

    @ApiModelProperty("报名时间")
    private Date createTime;

    @ApiModelProperty("用户信用")
    private Integer credit;

    @ApiModelProperty(value = "活动报名状态")
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(Integer regCount) {
        this.regCount = regCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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


    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }


    @Override
    public String toString() {
        return "RegMeetingListDto{" +
                "meetingId='" + meetingId + '\'' +
                ", title='" + title + '\'' +
                ", posterImagesUrl='" + posterImagesUrl + '\'' +
                ", meetingImagesUrl='" + meetingImagesUrl + '\'' +
                ", distance=" + distance +
                ", regCount=" + regCount +
                ", id='" + id + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", lv=" + lv +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", fee=" + fee +
                ", createTime=" + createTime +
                ", credit=" + credit +
                ", status=" + status +
                ", meetingType=" + meetingType +
                ", statusDescription='" + statusDescription + '\'' +
                '}';
    }
}
