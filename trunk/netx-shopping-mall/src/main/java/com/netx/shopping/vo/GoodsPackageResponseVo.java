package com.netx.shopping.vo;
/**
 * Created By liwei
 * Description: 获取商家包装明细返回对象
 * Date: 2017-09-14
 */
public class GoodsPackageResponseVo {
    /**
     * 标识ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 商品规格
     */
    private String spec;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
