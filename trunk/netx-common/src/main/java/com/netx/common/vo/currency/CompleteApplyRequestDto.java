package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created By liwei
 * Description: 网信立即竞购接口请求参数
 * Date: 2017-09-27
 */
@ApiModel
public class CompleteApplyRequestDto {

    @ApiModelProperty("可以多个网信id")
    @NotEmpty(message = "网信id不能为空")
    private List<String> currencyId;

    public List<String> getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(List<String> currencyId) {
        this.currencyId = currencyId;
    }
}
