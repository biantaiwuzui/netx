package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class AddValueRequestDto {

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty("属性值名")
    @NotBlank(message = "属性值名不能为空")
    private String name;

    @ApiModelProperty("属性id")
    @NotBlank(message = "属性id不能为空")
    private String propertyId;

    @ApiModelProperty("类目id")
    @NotBlank(message = "类目id不能为空")
    private String categoryId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
