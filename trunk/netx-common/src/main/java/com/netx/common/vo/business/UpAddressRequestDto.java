package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

public class UpAddressRequestDto extends AddressRequestDto {

    @ApiModelProperty("地址id：添加不用传")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
