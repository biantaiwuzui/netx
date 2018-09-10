package com.netx.common.vo.currency;

import com.netx.common.vo.common.FrozenAddRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By wj.liu
 * Description: 网信申购请求参数对象
 * Date: 2017-09-03
 */
@ApiModel
public class ApplyCurrencyRequestDto extends FrozenAddRequestDto{

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空 ")
    private String currency_Id;

    @ApiModelProperty("申购数量")
    @NotNull(message = "数量不能为空")
    private Double quantity;

    @ApiModelProperty("申购方式")
    @NotNull(message = "申购方式不能为空, 隐身申购为1，立即申购为2")
    @Min(value = 1)@Max(value = 2)
    private Integer way;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    public String getCurrency_Id() {
        return currency_Id;
    }

    public void setCurrency_Id(String currency_Id) {
        this.currency_Id = currency_Id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}