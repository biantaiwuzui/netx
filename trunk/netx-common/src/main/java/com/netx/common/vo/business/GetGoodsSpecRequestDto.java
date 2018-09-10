package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By hb.Lin
 * Description: 获取商品规格分类列表请求参数
 * Date: 2017-10-17
 */
@ApiModel
public class GetGoodsSpecRequestDto {
    @ApiModelProperty("用户id")
    @NotBlank(message="用户id不能为空")
    private String UserId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
