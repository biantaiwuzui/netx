package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 网信回购申请定时任务dto
 * @Author hj.Mao
 * @Date 2017-10-31
 */
@ApiModel
public class AutoBackBuyApplyRequestDto {

    @ApiModelProperty("网信ID")
    @NotBlank(message = "网信ID不能为空")
    private String currencyId;


    @ApiModelProperty("申请回购用户ID")
    @NotBlank(message = "申请回购用户ID不能为空")
    private String userId;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
