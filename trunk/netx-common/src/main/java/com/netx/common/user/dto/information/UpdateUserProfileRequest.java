package com.netx.common.user.dto.information;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("用户详情")
public class UpdateUserProfileRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("常驻")
    private String oftenIn;

    @ApiModelProperty("家乡")
    private String homeTown;

    @ApiModelProperty("去过")
    private String alreadyTo;

    @ApiModelProperty("想去")
    private String wantTo;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("介绍")
    private String introduce;

    @ApiModelProperty("性格")
    private String disposition;

    @ApiModelProperty("外貌")
    private String appearance;

    @ApiModelProperty("最低收入")
    private Integer income;

    @ApiModelProperty("最高收入")
    private Integer maxIncome;

    @ApiModelProperty("情感")
    private String emotion;

    @ApiModelProperty("身高")
    private Integer height;

    @ApiModelProperty("体重")
    private Integer weight;

    @ApiModelProperty("民族")
    private String nation;

    @ApiModelProperty("属相")
    private String animalSigns;

    @ApiModelProperty("星座")
    private String starSign;

    @ApiModelProperty("血型")
    private String bloodType;

    @ApiModelProperty("图文详情")
    private String description;

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

    public Integer getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Integer maxIncome) {
        this.maxIncome = maxIncome;
    }

    @Override
    public String toString() {
        return "UpdateUserProfileRequest{" +
                "userId='" + userId + '\'' +
                ", oftenIn='" + oftenIn + '\'' +
                ", homeTown='" + homeTown + '\'' +
                ", alreadyTo='" + alreadyTo + '\'' +
                ", wantTo='" + wantTo + '\'' +
                ", address='" + address + '\'' +
                ", introduce='" + introduce + '\'' +
                ", disposition='" + disposition + '\'' +
                ", appearance='" + appearance + '\'' +
                ", income=" + income +
                ", maxIncome=" + maxIncome +
                ", emotion='" + emotion + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", nation='" + nation + '\'' +
                ", animalSigns='" + animalSigns + '\'' +
                ", starSign='" + starSign + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
