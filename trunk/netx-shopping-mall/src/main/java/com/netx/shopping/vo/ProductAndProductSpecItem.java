package com.netx.shopping.vo;

import com.netx.shopping.model.product.Product;

import java.util.List;

public class ProductAndProductSpecItem extends Product {
    /**
     * 自选分类名称
     */
    private String kindName;
    /**
     * 系统规格分类名称
     */
    private String categoryName;

    private double distance;

    /**
     * 商品规格列表
     */
    private List<ProductSpecResponseVo> specList;

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductSpecResponseVo> getSpecList() {
        return specList;
    }

    public void setSpecList(List<ProductSpecResponseVo> specList) {
        this.specList = specList;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}