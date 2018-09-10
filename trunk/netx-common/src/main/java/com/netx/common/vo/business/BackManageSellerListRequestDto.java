package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * Created By liwei
 * Description: 商家后台管理商家黑白名单列表请求参数
 * Date: 2017-09-21
 */
@ApiModel
public class BackManageSellerListRequestDto extends PageRequestDto{

    @ApiModelProperty("商家名称")
    private String name;

    @ApiModelProperty("商家状态  1：白名单列表 2：黑名单列表 ")
    @NotNull(message = "商家状态不能为空")
    @Range(min = 1l,max = 2l,message = "商家状态不合法")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
