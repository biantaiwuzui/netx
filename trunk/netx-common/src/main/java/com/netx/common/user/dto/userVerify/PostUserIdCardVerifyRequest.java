package com.netx.common.user.dto.userVerify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("身份认证请求信息")
public class PostUserIdCardVerifyRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @Length(min = 2, max = 6, message = "用户真实姓名不在姓名长度范围内")
    @NotBlank(message = "用户真实姓名不能为空")
    @ApiModelProperty("用户真实姓名")
    private String realName;

    @Length(min = 18, max = 18, message = "身份证号不在身份证号长度的范围内")
    @NotBlank(message = "身份证号不能为空")
    @ApiModelProperty("身份证号")
    private String idCardNumber;

    @NotBlank(message = "身份证正面照片url不能为空")
    @ApiModelProperty("身份证正面照片")
    private String frontPhotoUrl;

    @NotBlank(message = "身份证背面照片url不能为空")
    @ApiModelProperty("身份证背面照片")
    private String backPhotoUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getFrontPhotoUrl() {
        return frontPhotoUrl;
    }

    public void setFrontPhotoUrl(String frontPhotoUrl) {
        this.frontPhotoUrl = frontPhotoUrl;
    }

    public String getBackPhotoUrl() {
        return backPhotoUrl;
    }

    public void setBackPhotoUrl(String backPhotoUrl) {
        this.backPhotoUrl = backPhotoUrl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
