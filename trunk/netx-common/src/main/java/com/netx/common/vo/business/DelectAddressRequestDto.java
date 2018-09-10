package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 删除收货地址请求参数对象
 * Date: 2018-01-29
 */
@ApiModel
public class DelectAddressRequestDto {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty(value = "地址列表下标，0开始")
    private long index;

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
}
