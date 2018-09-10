package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItemListResponseDto {

    /**
     * 订单id
     */
    private String id;

    /**
     * 商家id
     */
    private String merchantId;

    /**
     * 商家名
     */
    private String name;

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

    /**
     * 商品列表
     */
    private List<OrderProductBean> products = new ArrayList<>();
    
    @ApiModelProperty(value = "订单号Id")
    private String orderNo;
    
    @ApiModelProperty(value = "与商家的距离")
    private double distance;
    

    private Integer deliveryWay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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
