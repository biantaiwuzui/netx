package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreditPayDto {

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空")
    private String id;

    @ApiModelProperty("网信金额")
    @NotNull(message="网信支付金额不能为空")
    @Min(value=0,message = "支付金额最少0元")
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
