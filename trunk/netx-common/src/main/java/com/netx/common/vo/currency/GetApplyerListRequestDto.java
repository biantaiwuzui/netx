package com.netx.common.vo.currency;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 获取网信申购者列表
 */
@ApiModel
public class GetApplyerListRequestDto extends PageRequestDto{
    @ApiModelProperty
    @NotBlank(message="网信id不能为空")
    private String currencyId;

    @ApiModelProperty("申购方式：1.显示隐身申购  2.不显示立即申购（发行者）")
    private Integer way;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }
}
