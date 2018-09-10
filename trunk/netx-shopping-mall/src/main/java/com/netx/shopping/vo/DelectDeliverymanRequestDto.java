package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import java.util.List;

public class DelectDeliverymanRequestDto {
    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty(value = "配送人员列表下标，0开始")
    private String value;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
