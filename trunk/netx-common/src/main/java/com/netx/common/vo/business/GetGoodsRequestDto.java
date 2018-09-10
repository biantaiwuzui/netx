package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 商品详情请求参数对象
 * Date: 2017-09-14
 */
@ApiModel
public class GetGoodsRequestDto {

    @ApiModelProperty("商品id")
    @NotBlank(message = "商品id不能为空")
    private String id;


    @ApiModelProperty("用户id")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
