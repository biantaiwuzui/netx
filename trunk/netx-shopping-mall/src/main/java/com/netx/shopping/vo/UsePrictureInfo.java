package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

public class UsePrictureInfo {
    @ApiModelProperty(value = "图片id")
    private String id;

    @ApiModelProperty(value = "图片url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
