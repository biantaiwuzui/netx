package com.netx.credit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lanyingchu
 * @date 2018/7/30 9:59
 */
@ApiModel("获取我的网信")
public class SelectCreditDto {

    @ApiModelProperty("地址 - 经度")
    @NotNull(message = "地址-经度不能为空")
    private BigDecimal lon;

    @ApiModelProperty("地址 - 纬度")
    @NotNull(message = "地址-纬度不能为空")
    private BigDecimal lat;

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
