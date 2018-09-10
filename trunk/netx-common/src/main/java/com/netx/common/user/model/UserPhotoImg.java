package com.netx.common.user.model;

import io.swagger.annotations.ApiModelProperty;

public class UserPhotoImg {
    @ApiModelProperty("图片id")
    private String id;

    @ApiModelProperty("图片")
    private String url;

    @ApiModelProperty("图片位置")
    private Integer position;

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "UserPhotoImgs{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
