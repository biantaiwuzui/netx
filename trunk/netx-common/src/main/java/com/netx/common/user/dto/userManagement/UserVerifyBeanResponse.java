package com.netx.common.user.dto.userManagement;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserVerifyBeanResponse {
    @ApiModelProperty("视频认证")
    private List<SelectUserVerifyResourceResponse> videoResource;

    @ApiModelProperty("真实姓名+身份证号")
    private String idNumber;
    @ApiModelProperty("身份证认证资源")
    private List<SelectUserVerifyResourceResponse> idCardResource;

    //private String carBrand;
    @ApiModelProperty("车辆认证资源")
    private List<SelectUserVerifyResourceResponse> carResource;

    @ApiModelProperty("房产认证资源")
    private List<SelectUserVerifyResourceResponse> houseResource;

    @ApiModelProperty("学位认证资源")
    private List<SelectUserVerifyResourceResponse> degreeResource;

    public List<SelectUserVerifyResourceResponse> getVideoResource() {
        return videoResource;
    }

    public void setVideoResource(List<SelectUserVerifyResourceResponse> videoResource) {
        this.videoResource = videoResource;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public List<SelectUserVerifyResourceResponse> getIdCardResource() {
        return idCardResource;
    }

    public void setIdCardResource(List<SelectUserVerifyResourceResponse> idCardResource) {
        this.idCardResource = idCardResource;
    }

    public List<SelectUserVerifyResourceResponse> getCarResource() {
        return carResource;
    }

    public void setCarResource(List<SelectUserVerifyResourceResponse> carResource) {
        this.carResource = carResource;
    }

    public List<SelectUserVerifyResourceResponse> getHouseResource() {
        return houseResource;
    }

    public void setHouseResource(List<SelectUserVerifyResourceResponse> houseResource) {
        this.houseResource = houseResource;
    }

    public List<SelectUserVerifyResourceResponse> getDegreeResource() {
        return degreeResource;
    }

    public void setDegreeResource(List<SelectUserVerifyResourceResponse> degreeResource) {
        this.degreeResource = degreeResource;
    }
}
