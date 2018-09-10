package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-10-10
 */
@ApiModel
public class FrozenCashDepositRequestDto {
    @ApiModelProperty(value = "参与事件id,类似活动id或购买商品的订单id,与冻结时id一样", required = true)
    @NotNull
    private String typeId;
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull
    private String userId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
