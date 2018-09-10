package com.netx.common.router.dto.bean;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class UserInfoResponseDto {
    @ApiModelProperty("用户id")
    private String userId;

    //========== 1、用户表 ==========
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
    /*@ApiModelProperty("身份证号")
    private String idNumber;
    @ApiModelProperty("视频信息")
    private String video;
    @ApiModelProperty("车辆信息")
    private String car;
    @ApiModelProperty("房产信息")
    private String house;
    @ApiModelProperty("学历信息")
    private String degree;*/

    //------ 综合信息 ------
    /*@ApiModelProperty("文化教育概况")
    private String educationLabel;
    @ApiModelProperty("工作经历概况")
    private String professionLabel;
    @ApiModelProperty("兴趣爱好概况")
    private String interestLabel;*/

    //------ 网名信息 ------
    @ApiModelProperty("总积分")
    private BigDecimal score;
    @ApiModelProperty("总信用")
    private Integer credit;
    @ApiModelProperty("总身价")
    private BigDecimal value;
    @ApiModelProperty("总收益")
    private BigDecimal income;
    @ApiModelProperty("总贡献")
    private BigDecimal contribution;

    //------ 其他信息 ------
    @ApiModelProperty("乐观锁")
    private Integer lockVersion;

    @ApiModelProperty("最后登录时间")
    private Long lastLoginAt;
    /*@ApiModelProperty("连续登录天数")
    private Integer loginDays;*/
    @ApiModelProperty("礼物设置")
    private Integer giftSetting;
    @ApiModelProperty("邀请设置")
    private Integer invitationSetting;
    @ApiModelProperty("角色")
    private String role;

    //========== 2、用户详情表 ==========
    @ApiModelProperty("常驻")
    private String oftenIn;
    @ApiModelProperty("性格")
    private String disposition;

    //========== 3、用户照片表 ==========
    @ApiModelProperty("头像")
    private String headImgUrl;

	//====== 1、用户表 ======
    //------ 基本信息 ------

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

    //------ 认证信息 ------

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /*
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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
    }*/

    //------ 综合信息 ------

    /*public String getEducationLabel() {
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
    }*/

    //------ 网名信息 ------

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

    //------ 其他信息 ------

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    /*public Integer getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(Integer loginDays) {
        this.loginDays = loginDays;
    }*/

    public Integer getGiftSetting() {
        return giftSetting;
    }

    public void setGiftSetting(Integer giftSetting) {
        this.giftSetting = giftSetting;
    }

    public Integer getInvitationSetting() {
        return invitationSetting;
    }

    public void setInvitationSetting(Integer invitationSetting) {
        this.invitationSetting = invitationSetting;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //====== 2、用户详情表 ======
    public String getOftenIn() {
        return oftenIn;
    }

    public void setOftenIn(String oftenIn) {
        this.oftenIn = oftenIn;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    //====== 3、用户照片表 ======
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

}
