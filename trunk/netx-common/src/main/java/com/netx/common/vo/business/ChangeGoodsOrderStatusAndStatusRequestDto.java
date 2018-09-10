package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ChangeGoodsOrderStatusAndStatusRequestDto {

    @ApiModelProperty(value = "订单id", required = true)
    private List<String> orderId;

    @ApiModelProperty(value = "收货地址", required = true)
    private String address;

    @ApiModelProperty(value = "配送方式\n" +
            "1：第三方配送\n" +
            "2：不提供配送，现场消费\n" +
            "3：外卖配送", required = true)
    private List<Integer> deliveryWay;

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
     9: 待生成
     10: 已付款
     11: 已服务
     */
    @ApiModelProperty("订单状态:1：待付款,2：待发货,3：物流中,4：退货中,5：投诉中,6：待评论,7：已完成,8：已取消,9: 待生成,10: 已付款,11: 已服务")
    @NotNull
    private Integer status;

    @ApiModelProperty("订单备注")
    private Map<String,String> remark;

    public List<String> getOrderId() {
        return orderId;
    }

    public void setOrderId(List<String> orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(List<Integer> deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, String> getRemark() {
        return remark;
    }

    public void setRemark(Map<String, String> remark) {
        this.remark = remark;
    }
}
