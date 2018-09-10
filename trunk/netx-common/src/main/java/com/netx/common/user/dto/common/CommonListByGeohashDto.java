package com.netx.common.user.dto.common;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CommonListByGeohashDto {

    @NotNull(message = "经度不能为空")
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    @ApiModelProperty(value = "长度范围。比如length为5，代表搜索附近5公里<br>" +
            "1：5,009.4km ~ 4,992.6km<br>" +
            "2：1,252.3km ~ 624.1km<br>" +
            "3：156.5km ~ 156km<br>" +
            "4：39.1km ~ 19.5km<br>" +
            "5：4.9km ~ 4.9km<br>" +
            "6：1.2km ~ 609.4m<br>" +
            "7：152.9m ~ 152.4m<br>" +
            "8：38.2m ~ 19m<br>" +
            "9：4.8m ~ 4.8m<br>" +
            "10：1.2m ~ 59.5cm<br>" +
            "11：14.9cm ~ 14.9cm<br>" +
            "12：3.7cm ~ 1.9cm")
    @Min(value = 1, message = "最多只能找附近5,009.4km左右远的事物")
    @Max(value = 7, message = "最少只能找附近152.9m左右远的事物")
    private Integer length;

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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
