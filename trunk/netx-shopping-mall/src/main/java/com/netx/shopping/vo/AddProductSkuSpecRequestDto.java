package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class AddProductSkuSpecRequestDto {

    @ApiModelProperty("规格id，发布不填")
    private String id;

    @ApiModelProperty("库存id，发布不填")
    private String skuId;

    @ApiModelProperty("属性id")
    //@NotBlank(message = "属性id不能为空")
    private String propertyId;

    @ApiModelProperty("属性值id")
    //@NotBlank(message = "属性值id不能为空")
    private String valueId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }
}
