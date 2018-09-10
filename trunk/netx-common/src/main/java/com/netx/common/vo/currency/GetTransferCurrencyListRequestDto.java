package com.netx.common.vo.currency;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 获取转让网信列表请求参数
 * Date: 2017-09-21
 */
@ApiModel
public class GetTransferCurrencyListRequestDto extends PageRequestDto{

    @ApiModelProperty("用户id, 不为空为我转让的网信")
    private String userId;

    @ApiModelProperty("距离的用户ids")
    private List<String> distanceUserIds;

    @ApiModelProperty("网信名称")
    private String name;

    @ApiModelProperty("网信标签")
    private String tag;

    @ApiModelProperty("发行单价")
    private Integer faceValue;

    @ApiModelProperty("回购系数")
    private BigDecimal buyFactor;

    @ApiModelProperty("分红比例")
    private Integer royaltyRatio;

    @ApiModelProperty("发行状态, 1：正在发行; 2: 发行完成")
    private Integer status;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getDistanceUserIds() {
        return distanceUserIds;
    }

    public void setDistanceUserIds(List<String> distanceUserIds) {
        this.distanceUserIds = distanceUserIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public BigDecimal getBuyFactor() {
        return buyFactor;
    }

    public void setBuyFactor(BigDecimal buyFactor) {
        this.buyFactor = buyFactor;
    }

    public Integer getRoyaltyRatio() {
        return royaltyRatio;
    }

    public void setRoyaltyRatio(Integer royaltyRatio) {
        this.royaltyRatio = royaltyRatio;
    }

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
