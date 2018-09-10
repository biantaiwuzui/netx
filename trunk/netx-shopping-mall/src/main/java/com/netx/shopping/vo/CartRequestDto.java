package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CartRequestDto extends ProductOrderBean{

    @ApiModelProperty(value = "购物车的清单id：加入购物车时，这个为空", required = true)
    @NotBlank(message = "清单id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
