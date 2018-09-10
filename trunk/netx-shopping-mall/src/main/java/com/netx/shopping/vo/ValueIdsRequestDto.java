package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ValueIdsRequestDto {

    @ApiModelProperty("属性valueIds")
    @NotNull(message = "属性valueIds不能为空")
    private List<String> valueIds;

    @ApiModelProperty("商品id")
    @NotNull(message = "商品id不能为空")
    private String productId;

    public List<String> getValueIds() {
        return valueIds;
    }

    public void setValueIds(List<String> valueIds) {
        this.valueIds = valueIds;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
