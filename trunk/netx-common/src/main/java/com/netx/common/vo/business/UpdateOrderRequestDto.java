package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 修改订单信息接口请求参数
 * Date: 2017-10-14
 */
@ApiModel
public class UpdateOrderRequestDto {

    @ApiModelProperty(required = true, value = "订单id")
    @NotBlank(message = "订单id不能为空")
    private String id;

    @ApiModelProperty("收货地址")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
