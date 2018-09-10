package com.netx.searchengine.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by CloudZou on 2/10/18.
 */
public class UserSearchResponse {
    /**
     * 用户id
     */
    private String id;
    /**
     * 网号
     */
    private String userNumber;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private String sex;

    /**
     * 出生年月日【年龄可以通过这个获取】
     */
    private Date birthday;
    /**
     * 等级
     */
    private Integer lv;
    /**
     * 头像
     */
    private String headImgUrl;
    /**
     * 信用
     */
    private Integer credit;
    /**
     * 最后登录时间
     */
    private Date lastLoginAt;

    /**
     * 身高
     */
    private Integer height;
    /**
     * 手机号
     */
    private String mobile;
    private String video;
    private String car;
    private String house;
    private String degree;
    private String idNumber;

    /**
     * 教育概况
     */
    private String education;
    /**
     * 地址
     */
    private String address;
    /**
     * 个性标签
     */
    private String disposition;
    /**
     * 是否附近显示
     */
    private Integer nearlySetting;
    /**
     * 最后操作时间
     */
    private Date lastActiveAt;
    /**
     * 距离
     */
    private Double distance;

    /**
     * 学历
     */
    private List<String> degreeItem;

    /**
     * 常驻
     */
    private String oftenIn;

    /**
     * 家乡
     */
    private String homeTown;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 乐观锁版本
     */
    private Integer lockVersion;

    private Boolean isLogin;

    private BigDecimal lon;

    private BigDecimal lat;

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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public Integer getNearlySetting() {
        return nearlySetting;
    }

    public void setNearlySetting(Integer nearlySetting) {
        this.nearlySetting = nearlySetting;
    }

    public Date getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Date lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<String> getDegreeItem() {
        return degreeItem;
    }

    public void setDegreeItem(List<String> degreeItem) {
        this.degreeItem = degreeItem;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getOftenIn() {
        return oftenIn;
    }

    public void setOftenIn(String oftenIn) {
        this.oftenIn = oftenIn;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
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

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }
}

