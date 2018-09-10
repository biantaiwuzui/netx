package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CurrencyHomePageRequestDto {
    @ApiModelProperty("1正在申购用户 2正在转让的网信 3正在发行的网信")
    private Integer status;

    @ApiModelProperty("经度")
    @NotNull
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    @NotNull
    private BigDecimal lat;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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
