package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class GetSellerByUserIdRequestDto {

    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("1为经营商家，2为收藏商家，3为注册商家")
    private int index;

    public GetSellerByUserIdRequestDto() {
    }

    public GetSellerByUserIdRequestDto(String userId, int index) {
        this.userId = userId;
        this.index = index;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
