package com.netx.shopping.vo;

import com.netx.shopping.model.product.ProductSpec;
import com.netx.shopping.model.product.ProductSpecItem;

import java.util.List;

/**
 * Created By liwei
 * Description: 获取商家规格分类
 * Date: 2017-09-14
 */
public class ProductSpecResponseVo extends ProductSpec {


    /**
     * 商品规格项列表
     */
    private List<ProductSpecItem> itemList;

    public List<ProductSpecItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ProductSpecItem> itemList) {
        this.itemList = itemList;
    }
}
