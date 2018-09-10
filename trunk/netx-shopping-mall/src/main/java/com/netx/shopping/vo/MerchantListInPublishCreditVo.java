package com.netx.shopping.vo;

/**
 * @author lanyingchu
 * @date 2018/7/17 11:30
 */
public class MerchantListInPublishCreditVo {
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
    private String categories;
    /**
     * 商家图片
     */
    private String merchantImage;

    /**
     * choose标识
     */
    private Boolean status;

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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getMerchantImage() {
        return merchantImage;
    }

    public void setMerchantImage(String merchantImage) {
        this.merchantImage = merchantImage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
