package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;


/**
 * @author 梓
 * @date 2018-08-02 14:23
 */

/**
 * 发布者详情
 */
public class CreditPublisherInformationDto {

    @ApiModelProperty("发布者头像")
    private String pictureUrl;

    @ApiModelProperty("发布者真实名称")
    private String userRealName;

    @ApiModelProperty("发布者信用值")
    private Integer userCredit;

    @ApiModelProperty("发布者手机验证(若已验证,则为true,否则为false,下同)")
    private Boolean userMobile;

    @ApiModelProperty("发布者身份证验证")
    private Boolean userIdNumber;

    @ApiModelProperty("发布者车证验证")
    private Boolean userCar;

    @ApiModelProperty("发布者房产证验证")
    private Boolean userHouse;

    @ApiModelProperty("发布者学位验证")
    private Boolean userDegree;

    @ApiModelProperty("资料完整度分值")
    private Integer userProfileScore;

    public Integer getUserProfileScore() {
        return userProfileScore;
    }

    public void setUserProfileScore(Integer userProfileScore) {
        this.userProfileScore = userProfileScore;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Integer getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(Integer userCredit) {
        this.userCredit = userCredit;
    }

    public Boolean getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(Boolean userMobile) {
        this.userMobile = userMobile;
    }

    public Boolean getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(Boolean userIdNumber) {
        this.userIdNumber = userIdNumber;
    }

    public Boolean getUserCar() {
        return userCar;
    }

    public void setUserCar(Boolean userCar) {
        this.userCar = userCar;
    }

    public Boolean getUserHouse() {
        return userHouse;
    }

    public void setUserHouse(Boolean userHouse) {
        this.userHouse = userHouse;
    }

    public Boolean getUserDegree() {
        return userDegree;
    }

    public void setUserDegree(Boolean userDegree) {
        this.userDegree = userDegree;
    }
}
