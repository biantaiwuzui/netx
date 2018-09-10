package com.netx.common.vo.business;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateUserDetailsRequestDto {

    @NotBlank(message = "用户id不能为空")
    private String userId;
    /**
     * 常驻
     */
    private String oftenIn;
    /**
     * 家乡
     */
    private String homeTown;
    /**
     * 去过
     */
    private String alreadyTo;
    /**
     * 想去
     */
    private String wantTo;
    /**
     * 地址
     */
    private String address;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 性格
     */
    private String disposition;
    /**
     * 外貌
     */
    private String appearance;
    /**
     * 收入
     */
    private Integer income;
    /**
     * 最大工资
     */
    private Integer maxIncome;
    /**
     * 情感
     */
    private String emotion;
    /**
     * 身高
     */
    private Integer height;
    /**
     * 体重
     */
    private Integer weight;
    /**
     * 民族
     */
    private String nation;
    /**
     * 属相
     */

    private String animalSigns;
    /**
     * 星座
     */
    private String starSign;
    /**
     * 血型
     */
    private String bloodType;
    /**
     * 图文详情
     */
    private String description;

    private String updateUserId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOftenIn() {
        return oftenIn;
    }

    public void setOftenIn(String oftenIn) {
        this.oftenIn = oftenIn;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getAlreadyTo() {
        return alreadyTo;
    }

    public void setAlreadyTo(String alreadyTo) {
        this.alreadyTo = alreadyTo;
    }

    public String getWantTo() {
        return wantTo;
    }

    public void setWantTo(String wantTo) {
        this.wantTo = wantTo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Integer maxIncome) {
        this.maxIncome = maxIncome;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAnimalSigns() {
        return animalSigns;
    }

    public void setAnimalSigns(String animalSigns) {
        this.animalSigns = animalSigns;
    }

    public String getStarSign() {
        return starSign;
    }

    public void setStarSign(String starSign) {
        this.starSign = starSign;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}


