package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class QueryArticleLimitedListRequestDto {

    @NotBlank(message = "网号不能为空")
    @ApiModelProperty("查找发布资讯的网号")
    private String userNetworkNum;

    public String getUserNetworkNum() {
        return userNetworkNum;
    }

    public void setUserNetworkNum(String userNetworkNum) {
        this.userNetworkNum = userNetworkNum;
    }
}
