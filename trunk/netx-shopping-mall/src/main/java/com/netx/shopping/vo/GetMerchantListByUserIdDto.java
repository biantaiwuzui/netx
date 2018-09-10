package com.netx.shopping.vo;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

public class GetMerchantListByUserIdDto extends PageRequestDto{

    @ApiModelProperty("用户userId")
    private String userId;

    @ApiModelProperty("当status=1根据用户id获取经营的商家" +
            "<br>当status=2根据用户id获取收藏的商家")
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
