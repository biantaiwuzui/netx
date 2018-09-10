package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel
public class GetSellersBuyIdResponseVo {
    /**
     * 地址-经度
     */
    private BigDecimal lon;
    /**
     * 地址-纬度
     */
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
