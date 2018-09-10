package com.netx.credit.vo;

import java.util.List;

/**
 * @author lanyingchu
 * @date 2018/8/13 14:50
 */
public class CreditCategoryVo {

    private String categoryId;

    private String name;

    private Integer creditCategoryStatus;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCreditCategoryStatus() {
        return creditCategoryStatus;
    }

    public void setCreditCategoryStatus(Integer creditCategoryStatus) {
        this.creditCategoryStatus = creditCategoryStatus;
    }
}
