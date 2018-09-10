package com.netx.common.vo.business;

import com.netx.common.vo.common.ArbitrationAppealRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 网商用户退货申诉dto
 * @Author hj.Mao
 * @Date 2017-11-22
 */
@ApiModel
public class UserAppealArbitrationRequestDto extends ArbitrationAppealRequestDto{
    @ApiModelProperty("订单ID")
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "UserAppealArbitrationRequestDto{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
