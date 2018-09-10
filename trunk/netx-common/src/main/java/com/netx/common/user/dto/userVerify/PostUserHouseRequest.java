package com.netx.common.user.dto.userVerify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("房产认证请求信息")
public class PostUserHouseRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "房产证照片url不能为空")
    @ApiModelProperty("房产证照片")
    private String propertyCertificateUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPropertyCertificateUrl() {
        return propertyCertificateUrl;
    }

    public void setPropertyCertificateUrl(String propertyCertificateUrl) {
        this.propertyCertificateUrl = propertyCertificateUrl;
    }

    @Override
    public String toString() {
        return "PostUserHouse{" +
                "userId='" + userId + '\'' +
                ", propertyCertificateUrl='" + propertyCertificateUrl + '\'' +
                '}';
    }
}
