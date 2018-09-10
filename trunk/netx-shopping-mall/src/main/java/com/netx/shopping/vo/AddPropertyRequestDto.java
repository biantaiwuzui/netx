package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class AddPropertyRequestDto {

    @ApiModelProperty("属性id，编辑必填")
    private String id;

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty("属性名")
    @NotBlank(message = "属性名不能为空")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
