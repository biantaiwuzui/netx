package com.netx.shopping.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItemResponseDto {

    /**
     * 订单id
     */
    private String id;

    /**
     * 物流费用
     */
    private BigDecimal shippingFee;

    /**
     * 商品总数
     */
    private Integer total = 0;

    /**
     * 订单总价
     */
    private BigDecimal orderPrice;

    /**
     * 商品总价
     */
    private BigDecimal productPrice;

    /**
     * 备注
     */
    private String remark;

    private Integer deliveryWay;

    /**
     * 商品列表
     */
    private List<OrderProductBean> products = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Integer getTotal() {
        return total;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductBean> products) {
        if(products!=null){
            this.products = products;
            this.total = products.size();
        }
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }
}
