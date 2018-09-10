package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by allen on 17-9-17
 */
@ApiModel
public class ReceivablesOrderPayRequestDto {

    @ApiModelProperty(value = "付款用户ID",required = true)
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "付款用户网币ID",required = true)
    @NotBlank
    private String currencyId;

    @ApiModelProperty(value = "收款订单ID",required = true)
    @NotBlank
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
