package com.netx.shopping.vo;

import com.netx.shopping.model.ordercenter.constants.PayTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 订单支付接口请求参数对象
 * Date: 2017-11-09
 */
@ApiModel
public class MerchantPayOrderRequestDto {

    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    @Size(min = 1,message = "订单id集数量最低为1")
    private List<String> orderId;

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty(value = "支付方式：" +
            "PT_NONE：零钱支付" +
            "PT_WECHAT：微信支付" +
            "PT_ALI：支付宝支付" +
            "PT_CREDIT：网信支付" +
            "PT_FUSE：网信与零钱混合支付" +
            "PT_WECHAT_FUSE：网信与微信混合支付" +
            "PT_ALI_FUSE：网信与支付宝混合支付", required = true)
    @NotNull(message = "支付方式不能为空")
    private PayTypeEnum payType;

    @ApiModelProperty(value = "零钱实付金额")
    @NotNull(message = "零钱支付金额不能为空")
    @Min(value = 0,message = "支付金额最少0元")
    private BigDecimal payPrices;

    @Valid
    @ApiModelProperty(value = "网信支付需要传：" +
            "key为网信id，value为实付金额")
    private List<CreditPayDto> creditPay;

    public List<String> getOrderId() {
        return orderId;
    }

    public void setOrderId(List<String> orderId) {
        this.orderId = orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public PayTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(PayTypeEnum payType) {
        this.payType = payType;
    }

    public BigDecimal getPayPrices() {
        return payPrices;
    }

    public void setPayPrices(BigDecimal payPrices) {
        this.payPrices = payPrices;
    }

    public List<CreditPayDto> getCreditPay() {
        return creditPay;
    }

    public void setCreditPay(List<CreditPayDto> creditPay) {
        this.creditPay = creditPay;
    }
}
