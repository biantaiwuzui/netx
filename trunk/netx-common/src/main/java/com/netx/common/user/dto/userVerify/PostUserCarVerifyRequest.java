package com.netx.common.user.dto.userVerify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("车辆认证请求信息")
public class PostUserCarVerifyRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @Length(min = 7, max = 7, message = "不在车辆牌号长度的范围内")
    @NotBlank(message = "车辆牌号不能为空")
    @ApiModelProperty("车辆牌号")
    private String carBrand;

    @NotBlank(message = "行驶证不能为空")
    @ApiModelProperty("行驶证")
    private String vehicleLicenseUrl;

    @NotBlank(message = "年检登记不能为空")
    @ApiModelProperty("年检登记")
    private String annualRegistrationUrl;

    @NotBlank(message = "驾驶证不能为空")
    @ApiModelProperty("驾驶证")
    private String driverLicenseUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getDriverLicenseUrl() {
        return driverLicenseUrl;
    }

    public void setDriverLicenseUrl(String driverLicenseUrl) {
        this.driverLicenseUrl = driverLicenseUrl;
    }

    public String getAnnualRegistrationUrl() {
        return annualRegistrationUrl;
    }

    public void setAnnualRegistrationUrl(String annualRegistrationUrl) {
        this.annualRegistrationUrl = annualRegistrationUrl;
    }

    public String getVehicleLicenseUrl() {
        return vehicleLicenseUrl;
    }

    public void setVehicleLicenseUrl(String vehicleLicenseUrl) {
        this.vehicleLicenseUrl = vehicleLicenseUrl;
    }

    @Override
    public String toString() {
        return "PostUserCarVerifyRequest{" +
                "userId='" + userId + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", vehicleLicenseUrl='" + vehicleLicenseUrl + '\'' +
                ", annualRegistrationUrl='" + annualRegistrationUrl + '\'' +
                ", driverLicenseUrl='" + driverLicenseUrl + '\'' +
                '}';
    }
}
