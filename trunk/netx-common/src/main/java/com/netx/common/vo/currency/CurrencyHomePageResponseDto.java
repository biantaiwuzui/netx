package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class CurrencyHomePageResponseDto {

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("申购金额")
    private BigDecimal amount;

    @ApiModelProperty("网信转让金额（以元 为单位）")
    private BigDecimal transferAmount;

    @ApiModelProperty("设置网信转让金额")
    private BigDecimal setTransferAmount;

    @ApiModelProperty("网信正面图片")
    private String frontStyle;

    @ApiModelProperty("网信id")
    private String id;
    /**
     * 回购系数
     */
    private BigDecimal buyFactor;

    /**
     *转让用户id
     */
    private String launchUserId;

    /**
     * 网信名称
     */
    private String name;

    /**
     * 网信面值
     */
    private Integer faceValue;

    /**
     * 申购单价
     */
    private Integer applyPrice;

    /**
     * 距离
     */
    private double distance;


    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("等级lv")
    private Integer lv;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getSetTransferAmount() {
        return setTransferAmount;
    }

    public void setSetTransferAmount(BigDecimal setTransferAmount) {
        this.setTransferAmount = setTransferAmount;
    }

    public String getFrontStyle() {
        return frontStyle;
    }

    public void setFrontStyle(String frontStyle) {
        this.frontStyle = frontStyle;
    }

    public BigDecimal getBuyFactor() {
        return buyFactor;
    }

    public void setBuyFactor(BigDecimal buyFactor) {
        this.buyFactor = buyFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaunchUserId() {
        return launchUserId;
    }

    public void setLaunchUserId(String launchUserId) {
        this.launchUserId = launchUserId;
    }
}
