package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created By liwei
 * Description: 新增或修改用户订单收货地址请求参数对象
 * Date: 2018-01-29
 */
@ApiModel
public class AddOrUpdateRequestDto {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("收货详情")
    private List<String> address;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
