package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

public class AddProductSpeRequertDto {
    @ApiModelProperty(value = "规格id,空为增加，不空为编辑")
    private String id;

    @ApiModelProperty(value = "规格名称")
    private String name;

    @ApiModelProperty(value = "价格")
    private Long price;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
