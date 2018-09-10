package com.netx.shopping.vo;

import com.netx.shopping.model.business.Category;

import java.util.List;

public class ProductCategoryVo {
    private List<Category> category;
    private List<Category> Tages;

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Category> getTages() {
        return Tages;
    }

    public void setTages(List<Category> tages) {
        Tages = tages;
    }
}
