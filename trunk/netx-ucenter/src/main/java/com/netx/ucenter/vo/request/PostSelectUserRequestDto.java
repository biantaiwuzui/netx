package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;

public class PostSelectUserRequestDto {
    @ApiModelProperty(value = "网号")
    private String userNumber;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "身份证号码")
    private String isNumber;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIsNumber() {
        return isNumber;
    }

    public void setIsNumber(String isNumber) {
        this.isNumber = isNumber;
    }

    @Override
    public String toString() {
        return "PostSelectUserRequestDto{" +
                "userNumber='" + userNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isNumber='" + isNumber + '\'' +
                '}';
    }
}
