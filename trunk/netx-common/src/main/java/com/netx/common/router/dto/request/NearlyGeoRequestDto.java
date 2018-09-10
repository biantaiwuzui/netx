package com.netx.common.router.dto.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class NearlyGeoRequestDto {
    @ApiModelProperty("经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180,max = 180,message = "经度输入不在范围内")
    private Double lon;

    @ApiModelProperty("纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -90,max = 90,message = "纬度输入不在范围内")
    private Double lat;

    @ApiModelProperty("距离")
    @NotNull(message = "距离不能为空")
    private Double redaius;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getRedaius() {
        return redaius;
    }

    public void setRedaius(Double redaius) {
        this.redaius = redaius;
    }
}
