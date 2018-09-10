package com.netx.common.router.dto.select;

import com.netx.common.router.dto.bean.UserPhotosResponseDto;
import com.netx.common.user.model.UserPhotoImg;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户信息Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectUserBeanResponseDto {
    //------ 基本信息 ------
    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("网号")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("等级")
    private Integer lv;

    @ApiModelProperty("标签")
    private String tag;

    @ApiModelProperty("资料分值")
    private Integer userProfileScore;

    @ApiModelProperty("信用")
    private Integer credit;

    @ApiModelProperty("最后登录时间")
    private Long lastLoginAt;

    @ApiModelProperty("常驻")
    private String oftenIn;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("相册")
    private List<UserPhotoImg> imgUrls;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    //------ 认证信息 ------
    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("视频信息")
    private String video;

    @ApiModelProperty("车辆信息")
    private String car;

    @ApiModelProperty("房产信息")
    private String house;

    @ApiModelProperty("学历信息")
    private String degree;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("是否关注")
    private Boolean isWatch;

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

    public Integer getUserProfileScore() {
        return userProfileScore;
    }

    public void setUserProfileScore(Integer userProfileScore) {
        this.userProfileScore = userProfileScore;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getOftenIn() {
        return oftenIn;
    }

    public void setOftenIn(String oftenIn) {
        this.oftenIn = oftenIn;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public List<UserPhotoImg> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<UserPhotoImg> imgUrls) {
        this.imgUrls = imgUrls;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Boolean getWatch() {
        return isWatch;
    }

    public void setWatch(Boolean watch) {
        isWatch = watch;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "SelectUserBeanResponseDto{" +
                "id='" + id + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", lv=" + lv +
                ", userProfileScore=" + userProfileScore +
                ", credit=" + credit +
                ", lastLoginAt=" + lastLoginAt +
                ", oftenIn='" + oftenIn + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", imgUrls=" + imgUrls +
                ", lon=" + lon +
                ", lat=" + lat +
                ", mobile='" + mobile + '\'' +
                ", video='" + video + '\'' +
                ", car='" + car + '\'' +
                ", house='" + house + '\'' +
                ", degree='" + degree + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", isWatch='" + isWatch + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
