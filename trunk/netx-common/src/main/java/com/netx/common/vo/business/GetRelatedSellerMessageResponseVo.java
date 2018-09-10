package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created By liwei
 * Description:获取经营、收藏商店数与订单数
 * Date: 2017-11-1
 */
public class GetRelatedSellerMessageResponseVo {


    /**
     * 经营的商店数
     */
    @ApiModelProperty("经营的商店数")
    private Integer manageSellers;

    /**
     * 收藏的商店数
     */
    @ApiModelProperty("收藏的商店数")
    private Integer collectSellers;

//    /**
//     * 下单总数
//     */
//    private Integer orders;


    public Integer getManageSellers() {
        return manageSellers;
    }

    public void setManageSellers(Integer manageSellers) {
        this.manageSellers = manageSellers;
    }

    public Integer getCollectSellers() {
        return collectSellers;
    }

    public void setCollectSellers(Integer collectSellers) {
        this.collectSellers = collectSellers;
    }

//    public Integer getOrders() {
//        return orders;
//    }
//
//    public void setOrders(Integer orders) {
//        this.orders = orders;
//    }
}
