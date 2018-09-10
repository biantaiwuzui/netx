package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

public class ExpressResponse {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "code")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ExpressResponse{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
