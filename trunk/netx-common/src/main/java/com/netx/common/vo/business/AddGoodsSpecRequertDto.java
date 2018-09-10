package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created By liwei
 * Description: 添加商品规格请求参数
 * Date: 2017-09-13
 */
@ApiModel
public class AddGoodsSpecRequertDto {
    @ApiModelProperty("商品规格id, 如果没有可选就为空")
    private String specId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("规格分类名称, 如果有可选为空")
    private String specName;

    @ApiModelProperty("商品规格分类项名称")
    private String specItemName;

    @ApiModelProperty("差价，0代表无差价")
    private BigDecimal price;

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecItemName() {
        return specItemName;
    }

    public void setSpecItemName(String specItemName) {
        this.specItemName = specItemName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
