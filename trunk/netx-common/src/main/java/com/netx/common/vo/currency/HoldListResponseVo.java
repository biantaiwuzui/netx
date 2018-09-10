package com.netx.common.vo.currency;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 网信持有者列表
 * @Author  毛浩俊
 * @Date    2017-10-26
 */
@ApiModel
public class HoldListResponseVo{

    /**
     * 持有ID
     */
    @ApiModelProperty("持有ID")
    private String holdId;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 网信id
     */
    @ApiModelProperty("网信id")
    private String currencyId;

    /**
     * 头像图片
     */
    @ApiModelProperty("头像图片")
    private String headImgUrl;

    /**
     * 日期字符串
     */
    @ApiModelProperty("日期字符串")
    private String newlyTime;

    /**
     * 兑付描述
     */
    @ApiModelProperty("兑付描述")
    private String description;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;
    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;

    /**
     * 用户等级
     */
    @ApiModelProperty("用户等级")
    private Integer lv;

    /**
     * 申购网信金额
     */
    @ApiModelProperty("申购网信金额")
    private BigDecimal applyAmount;

    /**
     * 持有网信金额
     */
    @ApiModelProperty("持有网信金额")
    private BigDecimal holdAmount;

    /**
     * 转让网信金额
     */
    @ApiModelProperty("转让网信金额")
    private BigDecimal price;

    /**
     * 兑付总金额
     */
    @ApiModelProperty("兑付总金额")
    private BigDecimal tradeAmount;

    /**
     * 余额
     */
    @ApiModelProperty("余额")
    private BigDecimal releaseAmount;

    /**
     * 距离
     */
    @ApiModelProperty("距离")
    private Double distance;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getHoldId() { return holdId; }

    public void setHoldId(String holdId) { this.holdId = holdId; }

    public String getNewlyTime() { return newlyTime; }

    public void setNewlyTime(String newlyTime) { this.newlyTime = newlyTime; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(BigDecimal releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


}
