package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created By liwei
 * Description: 获取商家可支付网信详情
 * Date: 2017-11-30
 */
@ApiModel
public class GetCanCurrencyMessage1ResquesDto {

    @ApiModelProperty("网币id")
    @NotBlank(message = "网币id不能为空")
    private String currencyId;

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty("收付款状态：1收款，2付款")
    @NotEmpty(message = "收付款状态：1收款，2付款")
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
