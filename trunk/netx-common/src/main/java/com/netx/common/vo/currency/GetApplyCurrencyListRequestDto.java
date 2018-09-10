package com.netx.common.vo.currency;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 获取我持有网信列表请求参数
 * Date: 2017-09-21
 */
@ApiModel
public class GetApplyCurrencyListRequestDto extends PageRequestDto{

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
