package com.netx.common.user.model;

import com.netx.common.user.util.ComputeAgeUtils;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserSynopsisData {

    private final Double noRealDistance = 999.0;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("出生年月日")
    private Date birthday;

    @ApiModelProperty("年龄")
    private int age;

    @ApiModelProperty("等级")
    private String lv;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("个性标签（以‘,’隔开）：性格")
    private String tag;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("距离")
    private Double distance;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("家乡")
    private String homeTown;

    @ApiModelProperty("信用")
    private Integer credit;

    @ApiModelProperty("最后登录时间")
    private Long lastLoginAt;

    @ApiModelProperty("最后操作时间")
    private Long lastActiveAt;

    @ApiModelProperty("登录状态")
    private Boolean isLogin;

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

    @ApiModelProperty("是否已经关注")
    private Boolean isWatch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
        this.age = ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public void setBirthday(String birthday){
        if(birthday!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                setBirthday(dateFormat.parse(birthday));
            }catch (Exception e){ }
        }
    }

    public int getAge() {
        return age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
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

    public Long getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Long lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public Boolean getWatch() {
        return isWatch;
    }

    public void setWatch(Boolean watch) {
        isWatch = watch;
    }

    @Override
    public String toString() {
        return "UserSynopsisData{" +
                "id='" + id + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", lv='" + lv + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", tag='" + tag + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", distance=" + distance +
                ", address='" + address + '\'' +
                ", homeTown='" + homeTown + '\'' +
                ", credit=" + credit +
                ", lastLoginAt=" + lastLoginAt +
                ", lastActiveAt=" + lastActiveAt +
                ", isLogin=" + isLogin +
                ", mobile='" + mobile + '\'' +
                ", video='" + video + '\'' +
                ", car='" + car + '\'' +
                ", house='" + house + '\'' +
                ", degree='" + degree + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", isWatch=" + isWatch +
                '}';
    }
}
