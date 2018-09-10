package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created By liwei
 * Description:获取已成功发出的订单数/发出的订单总数
 * Date: 2017-11-7
 */
public class GetRelatedOdersMessageResponseVo {
    /**
     * 订单总数
     */
    @ApiModelProperty("订单总数")
    private Integer oderSumQuantity;
    /**
     * 现有订单数量
     */
    @ApiModelProperty("现有订单数量")
    private Integer oderNowQuantity;

    public Integer getOderSumQuantity() {
        return oderSumQuantity;
    }

    public void setOderSumQuantity(Integer oderSumQuantity) {
        this.oderSumQuantity = oderSumQuantity;
    }

    public Integer getOderNowQuantity() {
        return oderNowQuantity;
    }

    public void setOderNowQuantity(Integer oderNowQuantity) {
        this.oderNowQuantity = oderNowQuantity;
    }
}
