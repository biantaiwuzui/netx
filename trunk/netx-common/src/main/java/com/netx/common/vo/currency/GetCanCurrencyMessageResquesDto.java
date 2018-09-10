package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 获取商家可支付网信详情
 * Date: 2017-11-30
 */
@ApiModel
public class GetCanCurrencyMessageResquesDto {

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty("持有者id")
    private String userId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
