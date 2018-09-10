package com.netx.shopping.vo;

import com.netx.shopping.model.business.Category;

import java.util.List;

public class SellerCategoryVo {

    private List<Category> categories;
    private List<Category> Tages;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getTages() {
        return Tages;
    }

    public void setTages(List<Category> tages) {
        Tages = tages;
    }
}
