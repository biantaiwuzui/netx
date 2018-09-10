package com.netx.common.vo.business;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

public class GetSellerWhiteRequestDto extends CommonListDto {
    @ApiModelProperty("商家名称")
    private String name;
    @ApiModelProperty("状态 1：白名单，2：黑名单")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
