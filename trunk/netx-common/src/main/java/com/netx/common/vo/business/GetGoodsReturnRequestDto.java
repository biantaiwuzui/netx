package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 获取退货详情请求参数对象
 * Date: 2017-09-09
 */
@ApiModel
public class GetGoodsReturnRequestDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "退货记录id不能为空")
    private String id;

    @ApiModelProperty(value = "用户id", required = true)
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
