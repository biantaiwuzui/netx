package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 黎子安
 */
public class UserGeoRequest {

    @ApiModelProperty(value = "是否类型")
    @NotNull(message = "类型不能为空")
    private Boolean type;

    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180l,max = 180l,message = "输入的经度不合法(-180,180)")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -85l,max = 85l,message = "输入的纬度不合法(-85,85)")
    private BigDecimal lat;

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "UserGeoRequest{" +
                "type=" + type +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
