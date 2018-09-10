package com.netx.common.vo.business;

/**
 * Created By liwei
 * Description:获取网币适用范围的商家商品数量
 * Date: 2017-11-7
 */
public class GetSellerGoodsQuantityResponseVo {
    /**
     * 适用范围商品数
     */
    private Integer rangeGoods;

    /**
     * 适用范围商家数
     */
    private Integer rangeSellers;

    public Integer getRangeGoods() {
        return rangeGoods;
    }

    public void setRangeGoods(Integer rangeGoods) {
        this.rangeGoods = rangeGoods;
    }

    public Integer getRangeSellers() {
        return rangeSellers;
    }

    public void setRangeSellers(Integer rangeSellers) {
        this.rangeSellers = rangeSellers;
    }
}
