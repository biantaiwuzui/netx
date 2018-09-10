package com.netx.common.vo.currency;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class GetHoldListRequestDto extends PageRequestDto {

    @ApiModelProperty("当前用户ID")
    @NotBlank(message = "当前用户ID不能为空")
    private String userId;

    @ApiModelProperty("网信ID,必填项")
    @NotBlank(message = "网信ID不能为空")
    private String currencyId;

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "GetHoldListRequestDto{" +
                "currencyId='" + currencyId + '\'' +
                '}';
    }
}
