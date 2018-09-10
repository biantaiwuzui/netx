package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 订单支付接口请求参数对象
 * Date: 2017-11-09
 */
@ApiModel
public class PayOrderRequestDto {

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "订单id", required = true)
    private List<String> orderId;

    @ApiModelProperty(value = "支付方式, 1、网币支付\n" +
            "    2、零钱支付\n" +
            "    4、网币+零钱支付", required = true)
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

    @ApiModelProperty(value = "是否活动里的支付，是填：1  不是：不用填", required = true)
    private Integer isPayOfActivity;

    @ApiModelProperty(value = "收货地址", required = true)
    private String address;

    @ApiModelProperty(value = "配送方式\n" +
            "1：第三方配送\n" +
            "2：不提供配送，现场消费\n" +
            "3：外卖配送", required = true)
    private List<Integer> deliveryWay;

    @ApiModelProperty(value = "零钱实付金额")
    private List<BigDecimal> payPrices;

    @ApiModelProperty(value = "网币支付需要传")
    private String currencyId;

    @ApiModelProperty(value = "网币实付金额")
    private BigDecimal netCurrency;

    @ApiModelProperty("订单备注")
    private Map<String,String> remark;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<BigDecimal> getPayPrices() {
        return payPrices;
    }

    public void setPayPrices(List<BigDecimal> payPrices) {
        this.payPrices = payPrices;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getNetCurrency() {
        return netCurrency;
    }

    public void setNetCurrency(BigDecimal netCurrency) {
        this.netCurrency = netCurrency;
    }

    public Integer getIsPayOfActivity() {
        return isPayOfActivity;
    }

    public void setIsPayOfActivity(Integer isPayOfActivity) {
        this.isPayOfActivity = isPayOfActivity;
    }

    public Map<String, String> getRemark() {
        return remark;
    }

    public void setRemark(Map<String, String> remark) {
        this.remark = remark;
    }
}
