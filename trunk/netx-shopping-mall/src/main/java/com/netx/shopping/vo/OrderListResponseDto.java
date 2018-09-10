package com.netx.shopping.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderListResponseDto {

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("商家id")
    private String merchantUserId;

    @ApiModelProperty("商家名称")
    private String name;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单状态")
    private int orderStatus;

    @ApiModelProperty("订单总额")
    private BigDecimal totalPrice;

    @ApiModelProperty("商品件数")
    private int total;

    @ApiModelProperty("下单者id")
    private String userId;

    @ApiModelProperty("下单者昵称")
    private String userName;

    @ApiModelProperty("物流费用")
    private BigDecimal shippingFee;

    @ApiModelProperty("配送方式")
    private Integer deliveryWay;

    @ApiModelProperty("快递名")
    private String shippingName;

    @ApiModelProperty(value = "订单号Id")
    private String orderNo;

    @ApiModelProperty(value = "与商家的距离")
    private double distance;

    @ApiModelProperty(value = "留言")
    private String remark;

    @ApiModelProperty(value = "物流信息")
    private List<Map<String,String>> shippingDetail;
    
    @ApiModelProperty(value = "下单时间")
    private Date orderTime;

    @ApiModelProperty("客服id")
    private String customeUserId;

    @ApiModelProperty("物流订单号")
    private String shippingLogisticsNo;


    public String getShippingLogisticsNo() {
        return shippingLogisticsNo;
    }

    public void setShippingLogisticsNo(String shippingLogisticsNo) {
        this.shippingLogisticsNo = shippingLogisticsNo;
    }

    public String getCustomeUserId() {
        return customeUserId;
    }

    public void setCustomeUserId(String customeUserId) {
        this.customeUserId = customeUserId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public List<Map<String, String>> getShippingDetail() {
        return shippingDetail;
    }

    public void setShippingDetail(List<Map<String, String>> shippingDetail) {
        this.shippingDetail = shippingDetail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTotal(int total) {
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

    private List<OrderProductBean> productBeans;

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotal() {
        return total;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public List<OrderProductBean> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(List<OrderProductBean> productBeans) {
        this.productBeans = productBeans;
        if(productBeans!=null){
            this.total = productBeans.size();
        }
    }
}