package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MerchantOrderDetailResponseDto {

    @ApiModelProperty("订单id")
    private String id;

    @ApiModelProperty("下单者id")
    private String userId;

    @ApiModelProperty("收银员id")
    private String cashierId;

    @ApiModelProperty("客服id")
    private String customeUserId;

    @ApiModelProperty("商家电话")
    private String phone;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty("注册者id")
    private String merchantUserId;

    @ApiModelProperty("是否为商家管理员")
    private Boolean isAdmin;

    @ApiModelProperty("商品列表")
    private List<OrderProductBean> productBeans;

    @ApiModelProperty("收货人姓名")
    private String consignee;

    @ApiModelProperty("收货人电话")
    private String mobile;

    @ApiModelProperty("收货人地址")
    private String fullAddress;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty("下单时间")
    private Date orderTime;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("配送方式")
    private Integer deliveryWay;

    @ApiModelProperty("快递名")
    private String shippingName;

    @ApiModelProperty("运费")
    private BigDecimal shippingFeeBig;

    @ApiModelProperty("总价")
    private BigDecimal totalBig;

    @ApiModelProperty("快递单号")
    private String shippingLogisticsNo;

    @ApiModelProperty("物流信息")
    private List<Map<String,String>> shippingDetail;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("与商家的距离")
    private double distance;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<OrderProductBean> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(List<OrderProductBean> productBeans) {
        this.productBeans = productBeans;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingLogisticsNo() {
        return shippingLogisticsNo;
    }

    public void setShippingLogisticsNo(String shippingLogisticsNo) {
        this.shippingLogisticsNo = shippingLogisticsNo;
    }

    public List<Map<String, String>> getShippingDetail() {
        return shippingDetail;
    }

    public void setShippingDetail(List<Map<String, String>> shippingDetail) {
        this.shippingDetail = shippingDetail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public BigDecimal getShippingFeeBig() {
        return shippingFeeBig;
    }

    public void setShippingFeeBig(BigDecimal shippingFeeBig) {
        this.shippingFeeBig = shippingFeeBig;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public BigDecimal getTotalBig() {
        return totalBig;
    }

    public void setTotalBig(BigDecimal totalBig) {
        this.totalBig = totalBig;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getCustomeUserId() {
        return customeUserId;
    }

    public void setCustomeUserId(String customeUserId) {
        this.customeUserId = customeUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

