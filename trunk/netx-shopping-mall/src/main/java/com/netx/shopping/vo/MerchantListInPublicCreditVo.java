package com.netx.shopping.vo;

import java.util.List;

/**
 * @author lanyingchu
 * @date 2018/7/17 11:30
 */
public class MerchantListInPublicCreditVo {
    /**
     * 商家id
     */
    private String id;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 商品一级类目
     */
    private List<String> categories;
    /**
     * 商品二级类目
     */
    private List<String> tages;
    /**
     * 商家图片
     */
    private String merchantImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTages() {
        return tages;
    }

    public void setTages(List<String> tages) {
        this.tages = tages;
    }

    public String getMerchantImage() {
        return merchantImage;
    }

    public void setMerchantImage(String merchantImage) {
        this.merchantImage = merchantImage;
    }
}
