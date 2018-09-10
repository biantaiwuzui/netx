package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created By liwe
 * Description: 用户申请延迟收货请求参数对象
 * Date: 2018-02-11
 */
@ApiModel
public class AgreePutOffRequestDto {

    @ApiModelProperty(value = "orderId", required = true)
    @NotBlank(message = "订单id不能为空")
    private String orderId;

    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空")
    private String sellerUserId;

    @ApiModelProperty(value = "商家是否同意延迟：0：同意，1拒接")
    private Integer isAgree;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public Integer getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(Integer isAgree) {
        this.isAgree = isAgree;
    }
}
