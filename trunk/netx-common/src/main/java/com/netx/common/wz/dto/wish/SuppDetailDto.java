package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class SuppDetailDto {

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

    @ApiModelProperty("出生年月日")
    private Date birthday;

    @ApiModelProperty("信用")
    private Integer credit;

    @ApiModelProperty("是否发行网信")
    private Boolean isPublishCredit;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "支持金额")
    private BigDecimal amount;
    /**
     * 支持金额
     */

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SuppDetailDto{" +
                "id='" + id + '\'' +
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
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", mobile='" + mobile + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", video='" + video + '\'' +
                ", car='" + car + '\'' +
                ", house='" + house + '\'' +
                ", degree='" + degree + '\'' +
                '}';
    }


}
