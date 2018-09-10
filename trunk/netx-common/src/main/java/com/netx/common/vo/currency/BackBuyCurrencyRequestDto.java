package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By wj.liu
 * Description: 网信回购请求参数对象
 * Date: 2017-09-03
 */
@ApiModel
public class BackBuyCurrencyRequestDto {

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空")
    private String currencyId;

    @ApiModelProperty("回购申请用户id")
    @NotBlank(message = "回购申请用户id不能为空")
    private String userId;

    @ApiModelProperty("回购网信金额")
    @NotNull(message = "回购网信金额不能为空")
    private BigDecimal price;

    @ApiModelProperty("回购收益")
    @NotNull(message = "回购收益不能为空")
    private BigDecimal income;


    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

}
