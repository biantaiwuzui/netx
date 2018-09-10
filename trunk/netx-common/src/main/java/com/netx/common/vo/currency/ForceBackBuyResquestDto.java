package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created By liwei
 * Description: 网信强制回购请求参数对象
 * Date: 2017-11-02
 */
@ApiModel
public class ForceBackBuyResquestDto {
    @ApiModelProperty("选择持有id")
    @NotEmpty(message = "持有id不能为空，可以多个id")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
