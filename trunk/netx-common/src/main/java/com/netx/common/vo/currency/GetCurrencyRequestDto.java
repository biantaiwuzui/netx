package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 获取网信详情请求参数
 * Date: 2017-09-13
 */
@ApiModel
public class GetCurrencyRequestDto {

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空")
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
