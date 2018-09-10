package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 是否有注册过商家名或发布过商品名接口请求参数对象
 * Date: 2017-11-05
 */
@ApiModel
public class IsHaveThisNameRequestDto {
    @ApiModelProperty(value = "商家id", required = true)
    private String merchantId;

    @ApiModelProperty(value = "商家名称/商品名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
