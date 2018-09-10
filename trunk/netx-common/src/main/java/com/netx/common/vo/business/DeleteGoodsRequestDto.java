package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 删除商品接口请求参数对象
 * Date: 2017-11-05
 */
@ApiModel
public class DeleteGoodsRequestDto {

    @ApiModelProperty(value = "操作用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "商品id", required = true)
    @NotBlank(message = "商品id不能为空")
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
