package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class SelectWangMingListBaseResponseDto {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String headImgUrl;

    @ApiModelProperty("身价")
    private BigDecimal value;

    @ApiModelProperty("收益")
    private BigDecimal income;

    @ApiModelProperty("贡献")
    private BigDecimal contribution;

    @ApiModelProperty("信用")
    private Integer credit;

    @ApiModelProperty("积分")
    private BigDecimal score;

    @ApiModelProperty("lv等级")
    private int lv;

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

    @ApiModelProperty("个性标签")
    private String tag;

    @ApiModelProperty("经度")
    private Double lon;

    @ApiModelProperty("维度")
    private Double lat;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
