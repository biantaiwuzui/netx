package com.netx.common.vo.business;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GetNewestOdersMessageResponseVo {
    /**
     * 标识ID
     */
    @ApiModelProperty("标识ID")
    private String id;
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;
    /**
     * 商家ID
     */
    @ApiModelProperty("商家ID")
    private String sellerId;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderNum;
    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal totalPrice;
    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal payPrice;
    /**
     * 订单备注
     */
    @ApiModelProperty("订单备注")
    private String remark;
    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Integer orderTime;
    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    private Integer payTime;
    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Integer sendTime;
    /**
     * 收货地址
     */
    @ApiModelProperty("收货地址")
    private String address;
    /**
     * 订单状态
     1：待付款
     2：待发货
     3：物流中
     4：退货中
     5：投诉中
     6：待评论
     7：已完成
     8：已取消
     */
    @ApiModelProperty("订单状态")
    private Integer status;
    /**
     * 支付方式
     1、网币支付
     2、零钱支付
     3、平台垫付
     */
    @ApiModelProperty("支付方式")
    private Integer payWay;
    /**
     * 网币金额
     */
    @ApiModelProperty("网币金额")
    private BigDecimal netCurrency;
    /**
     * 物流名称
     */
    @ApiModelProperty("物流名称")
    private String logisticsName;
    /**
     * 物流单号
     */
    @ApiModelProperty("物流单号")
    private String logisticsNum;
    /**
     * 物流状态
     1：物流中
     2：已完成
     */
    @ApiModelProperty("物流状态")
    private Integer logisticsStatus;

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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Integer orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPayTime() {
        return payTime;
    }

    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public BigDecimal getNetCurrency() {
        return netCurrency;
    }

    public void setNetCurrency(BigDecimal netCurrency) {
        this.netCurrency = netCurrency;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    public Integer getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }
}
