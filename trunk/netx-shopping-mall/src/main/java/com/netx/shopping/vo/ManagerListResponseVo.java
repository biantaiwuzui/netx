package com.netx.shopping.vo;

import java.util.Date;

public class ManagerListResponseVo {

    private String id;
    /**
     * 商家id
     */
    private String merchantId;
    /**
     * 商家注册者用户ID
     */
    private String userId;
    /**
     * 商家管理人员类型：法人代表，收银人员，业务主管
     */
    private String merchantUserType;
    /**
     * 收银人员姓名
     */
    private String userName;
    /**
     * 收银人手机号
     */
    private String userPhone;
    /**
     * 收银人网号
     */
    private String userNetworkNum;
    /**
     * 是否商家使用中： 0：不是   1：是
     */
    private Integer isCurrent;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private String sex;

    /**
     * 等级
     */
    private Integer lv;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 信用
     */
    private Integer credit;

    private String headImgUrl;

    private String idCard;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantUserType() {
        return merchantUserType;
    }

    public void setMerchantUserType(String merchantUserType) {
        this.merchantUserType = merchantUserType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserNetworkNum() {
        return userNetworkNum;
    }

    public void setUserNetworkNum(String userNetworkNum) {
        this.userNetworkNum = userNetworkNum;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
