package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By liwei
 * Description: 网商首页请求参数
 * Date: 2017-11-22
 */
@ApiModel
public class BusinessHomePageRequestDto extends PageRequestDto {
    @NotNull(message = "经度不能为空")
    private BigDecimal lon;
    @NotNull(message = "纬度不能为空")
    private BigDecimal lat;
//    @ApiModelProperty("距离范围值：" +
//            "-1:不限<br>" +
//            "1:1km<br>" +
//            "2:2km<br>" +
//            "3:3km<br>" +
//            "4:5km<br>" +
//            "5:10km<br>" +
//            "6：50km<br>" +
//            "7:100km<br>" +
//            "8:150km<br>" +
//            "9:200km<br>")
//    private Integer length;

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

//    public Integer getLength() {
//        return length;
//    }
//
//    public void setLength(Integer length) {
//        this.length = length;
//    }
}
