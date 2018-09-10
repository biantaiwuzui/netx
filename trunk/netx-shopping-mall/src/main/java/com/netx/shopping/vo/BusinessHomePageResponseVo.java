package com.netx.shopping.vo;



import java.util.List;

/**
 * Created By liwei
 * Description: 网商首页返回参数
 * Date: 2017-11-22
 */
public class BusinessHomePageResponseVo {

    /**
     * 附近商家列表
     */
    private List<GetSellerListResponseVo> sellerList;

    /**
     * 附近商品列表
     */
    private List<ProductAndProductSpecItem> goodsList;


    public List<GetSellerListResponseVo> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<GetSellerListResponseVo> sellerList) {
        this.sellerList = sellerList;
    }

    public List<ProductAndProductSpecItem> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ProductAndProductSpecItem> goodsList) {
        this.goodsList = goodsList;
    }

}