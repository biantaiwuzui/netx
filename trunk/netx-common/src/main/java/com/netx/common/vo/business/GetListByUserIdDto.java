package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class GetListByUserIdDto extends PageRequestDto {

    @ApiModelProperty("用户userId")
    private String userId;

    @ApiModelProperty("当status=1根据用户id获取经营的商品" +
            "<br>当status=2根据用户id获取收藏的商品")
    @NotNull(message = "当status不能为空")
    private Integer status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
