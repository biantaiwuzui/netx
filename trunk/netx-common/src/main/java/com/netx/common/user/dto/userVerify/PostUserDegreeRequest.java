package com.netx.common.user.dto.userVerify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("学历认证请求信息")
public class PostUserDegreeRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "学位证照片url不能为空")
    @ApiModelProperty("学位证照片")
    private String degreeCertificateUrl;

    @NotBlank(message = "学历证明照片url不能为空")
    @ApiModelProperty("学历证明照片")
    private String graduationCertificateUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDegreeCertificateUrl() {
        return degreeCertificateUrl;
    }

    public void setDegreeCertificateUrl(String degreeCertificateUrl) {
        this.degreeCertificateUrl = degreeCertificateUrl;
    }

    public String getGraduationCertificateUrl() {
        return graduationCertificateUrl;
    }

    public void setGraduationCertificateUrl(String graduationCertificateUrl) {
        this.graduationCertificateUrl = graduationCertificateUrl;
    }

    @Override
    public String toString() {
        return "PostUserDegreeRequest{" +
                "userId='" + userId + '\'' +
                ", degreeCertificateUrl='" + degreeCertificateUrl + '\'' +
                ", graduationCertificateUrl='" + graduationCertificateUrl + '\'' +
                '}';
    }
}
