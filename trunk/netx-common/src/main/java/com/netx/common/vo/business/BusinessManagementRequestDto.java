package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 商家管理请求参数
 * Date: 2017-11-20
 */
@ApiModel
public class BusinessManagementRequestDto {
    @ApiModelProperty(value = "商家id")
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
