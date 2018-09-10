package com.netx.ucenter.vo.response;

import com.netx.common.router.dto.bean.UserBeanResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户登录信息")
public class SelectLoginUserResponse {
    @ApiModelProperty("用户基本信息")
    private UserBeanResponseDto userBeanResponseDto;

    @ApiModelProperty("token值")
    private String token;

    public UserBeanResponseDto getUserBeanResponseDto() {
        return userBeanResponseDto;
    }

    public void setUserBeanResponseDto(UserBeanResponseDto userBeanResponseDto) {
        this.userBeanResponseDto = userBeanResponseDto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SelectLoginUserResponse{" +
                "userBeanResponseDto=" + userBeanResponseDto +
                ", token='" + token + '\'' +
                '}';
    }
}
