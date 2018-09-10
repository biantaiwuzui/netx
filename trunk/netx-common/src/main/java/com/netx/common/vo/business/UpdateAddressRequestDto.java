package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created By liwei
 * Description: 修改用户收货地址请求参数
 * Date: 2018-01-29
 */
@ApiModel
public class UpdateAddressRequestDto {

    @ApiModelProperty(required = true, value = "用户id")
    private String userId;

    @ApiModelProperty(value = "地址列表下标，0开始")
    private  long index;

    @ApiModelProperty(value = "地址详情")
    private List<String> address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
