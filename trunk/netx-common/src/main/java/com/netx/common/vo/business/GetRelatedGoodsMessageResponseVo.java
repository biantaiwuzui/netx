package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created By liwei
 * Description: 获取成功发布的商品/发布的商品总数,已成功发出的订单数/发出的订单总数
 * Date: 2017-11-1
 */
public class GetRelatedGoodsMessageResponseVo {
    /**
     * 商品发行的总数
     */
    @ApiModelProperty("商品发行的总数")
    private Integer sumQuantity = 0;
    /**
     * 经营商品总数
     */
    @ApiModelProperty("经营商品总数")
    private Integer nowQuantity = 0;

    /**
     * 关注商品的数量
     */
    @ApiModelProperty("关注商品的数量")
    private Integer goodsCollectQuantity = 0;

    public Integer getSumQuantity() {
        return sumQuantity;
    }

    public void setSumQuantity(Integer sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    public Integer getNowQuantity() {
        return nowQuantity;
    }

    public void setNowQuantity(Integer nowQuantity) {
        this.nowQuantity = nowQuantity;
    }

    public Integer getGoodsCollectQuantity() {
        return goodsCollectQuantity;
    }

    public void setGoodsCollectQuantity(Integer goodsCollectQuantity) {
        this.goodsCollectQuantity = goodsCollectQuantity;
    }
}
