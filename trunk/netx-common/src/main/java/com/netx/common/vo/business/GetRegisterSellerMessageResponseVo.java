package com.netx.common.vo.business;


import io.swagger.annotations.ApiModelProperty;

/**
 * Created By liwei
 * Description:获取经营、收藏商店数与订单数
 * Date: 2017-11-8
 */
public class GetRegisterSellerMessageResponseVo {

    /**
     * 现有注册的商店数
     */
    @ApiModelProperty("现有注册的商店数")
    private Integer nowRegisterSeller;

    /**
     * 总注册过的商店数
     */
    @ApiModelProperty("总注册过的商店数")
    private Integer sumRegisterSeller;

    public Integer getNowRegisterSeller() {
        return nowRegisterSeller;
    }

    public void setNowRegisterSeller(Integer nowRegisterSeller) {
        this.nowRegisterSeller = nowRegisterSeller;
    }

    public Integer getSumRegisterSeller() {
        return sumRegisterSeller;
    }

    public void setSumRegisterSeller(Integer sumRegisterSeller) {
        this.sumRegisterSeller = sumRegisterSeller;
    }
}
