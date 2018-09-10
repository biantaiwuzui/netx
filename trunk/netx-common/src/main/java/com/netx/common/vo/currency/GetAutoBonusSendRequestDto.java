package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 获取网信分红请求参数对象
 * Date: 2017-11-4
 */
@ApiModel
public class GetAutoBonusSendRequestDto {

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
