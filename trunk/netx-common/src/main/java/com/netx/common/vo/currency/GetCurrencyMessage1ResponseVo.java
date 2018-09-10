package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GetCurrencyMessage1ResponseVo {

    @ApiModelProperty("网信id")
    private String id;
    @ApiModelProperty("用户ID")
    private String userId;
    /**
     * 网信名称
     */
    @ApiModelProperty("网信名称")
    private String name;
    /**
     * 回购系数
     */
    @ApiModelProperty("回购系数")
    private BigDecimal buyFactor;
    /**
     * 递增幅度
     */
    @ApiModelProperty("递增幅度")
    private Integer growthRate;

    /**
     * 发行数量
     */
    @ApiModelProperty("发行数量")
    private Integer releaseNum;
    /**
     * 网信面值
     */
    @ApiModelProperty("网信面值")
    private Integer faceValue;
    /**
     * 申购单价
     */
    @ApiModelProperty("申购单价")
    private Integer applyPrice;
    /**
     * 选择范围商家，选择适用范围2的时候必填
     */
    @ApiModelProperty("适用范围商家id集")
    private String sellerIds;
    /**
     * 已申购金额
     */
    @ApiModelProperty("已申购金额")
    private BigDecimal buyAmount;
    /**
     * 已兑付金额
     */
    @ApiModelProperty("已兑付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("累计月分红")
    private BigDecimal money;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("等级lv")
    private Integer lv;
    @ApiModelProperty("商家剩余的未兑付网信总额")
    private BigDecimal getSellerRemainCurrencyAmount;
//    @ApiModelProperty("头像")
//    private String headImgUrl;

    @ApiModelProperty("持有金额")
    private BigDecimal amount;
    /**
     * 网信正面样式
     */
    @ApiModelProperty("网信正面样式")
    private String frontStyle;
    /**
     * 网信反面样式
     */
    @ApiModelProperty("网信反面样式")
    private String backStyle;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBuyFactor() {
        return buyFactor;
    }

    public void setBuyFactor(BigDecimal buyFactor) {
        this.buyFactor = buyFactor;
    }

    public Integer getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Integer growthRate) {
        this.growthRate = growthRate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    public BigDecimal getGetSellerRemainCurrencyAmount() {
        return getSellerRemainCurrencyAmount;
    }

    public void setGetSellerRemainCurrencyAmount(BigDecimal getSellerRemainCurrencyAmount) {
        this.getSellerRemainCurrencyAmount = getSellerRemainCurrencyAmount;
    }

//    public String getHeadImgUrl() {
//        return headImgUrl;
//    }
//
//    public void setHeadImgUrl(String headImgUrl) {
//        this.headImgUrl = headImgUrl;
//    }


    public String getFrontStyle() {
        return frontStyle;
    }

    public void setFrontStyle(String frontStyle) {
        this.frontStyle = frontStyle;
    }

    public String getBackStyle() {
        return backStyle;
    }

    public void setBackStyle(String backStyle) {
        this.backStyle = backStyle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getReleaseNum() {
        return releaseNum;
    }

    public void setReleaseNum(Integer releaseNum) {
        this.releaseNum = releaseNum;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public Integer getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(Integer applyPrice) {
        this.applyPrice = applyPrice;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(String sellerIds) {
        this.sellerIds = sellerIds;
    }
}
