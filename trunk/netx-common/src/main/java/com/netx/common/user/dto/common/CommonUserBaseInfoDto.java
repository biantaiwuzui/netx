package com.netx.common.user.dto.common;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class CommonUserBaseInfoDto {

    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("乐观锁")
    private Integer lockVersion;
    @ApiModelProperty("头像")
    private String headImgUrl;
    @ApiModelProperty("最后登录时间")
    private Long lastLoginAt;

    //------ 基本信息 ------
    @ApiModelProperty("网号")
    private String userNumber;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("等级lv")
    private Integer lv;
    @ApiModelProperty("资料分值")
    private Integer userProfileScore;

    //------ 认证信息 ------
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("身份证号")
    private String idNumber;
    @ApiModelProperty("视频信息")
    private String video;
    @ApiModelProperty("车辆信息")
    private String car;
    @ApiModelProperty("房产信息")
    private String house;
    @ApiModelProperty("学历信息")
    private String degree;

    //------ 综合信息 ------
    @ApiModelProperty("文化教育概况")
    private String educationLabel;
    @ApiModelProperty("工作经历概况")
    private String professionLabel;
    @ApiModelProperty("兴趣爱好概况")
    private String interestLabel;

    //------ 网名信息 ------
    @ApiModelProperty("总积分")
    private Integer score;
    @ApiModelProperty("总信用")
    private Integer credit;
    @ApiModelProperty("总身价")
    private BigDecimal value;
    @ApiModelProperty("总收益")
    private BigDecimal income;
    @ApiModelProperty("总贡献")
    private BigDecimal contribution;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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

    public Integer getUserProfileScore() {
        return userProfileScore;
    }

    public void setUserProfileScore(Integer userProfileScore) {
        this.userProfileScore = userProfileScore;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getEducationLabel() {
        return educationLabel;
    }

    public void setEducationLabel(String educationLabel) {
        this.educationLabel = educationLabel;
    }

    public String getProfessionLabel() {
        return professionLabel;
    }

    public void setProfessionLabel(String professionLabel) {
        this.professionLabel = professionLabel;
    }

    public String getInterestLabel() {
        return interestLabel;
    }

    public void setInterestLabel(String interestLabel) {
        this.interestLabel = interestLabel;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getContribution() {
        return contribution;
    }

    public void setContribution(BigDecimal contribution) {
        this.contribution = contribution;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    @Override
    public String toString() {
        return "CommonUserBaseInfoDto{" +
                "userId='" + userId + '\'' +
                ", lockVersion=" + lockVersion +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", lastLoginAt=" + lastLoginAt +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", lv=" + lv +
                ", userProfileScore=" + userProfileScore +
                ", mobile='" + mobile + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", video='" + video + '\'' +
                ", car='" + car + '\'' +
                ", house='" + house + '\'' +
                ", degree='" + degree + '\'' +
                ", educationLabel='" + educationLabel + '\'' +
                ", professionLabel='" + professionLabel + '\'' +
                ", interestLabel='" + interestLabel + '\'' +
                ", score=" + score +
                ", credit=" + credit +
                ", value=" + value +
                ", income=" + income +
                ", contribution=" + contribution +
                '}';
    }
}
