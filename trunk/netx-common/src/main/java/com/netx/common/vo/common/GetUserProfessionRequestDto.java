package com.netx.common.vo.common;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

public class GetUserProfessionRequestDto extends CommonListDto {

    @ApiModelProperty("用户id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
