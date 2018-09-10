package com.netx.common.user.dto.userManagement;

import io.swagger.annotations.ApiModelProperty;

public class SelectUserVerifyResourceResponse {

    @ApiModelProperty("关联的认证id")
    private String userVerifyId;

    @ApiModelProperty("视频或图片url")
    private String url;

    @ApiModelProperty("位置（值越小，排序越优先）")
    private Integer position;

    public String getUserVerifyId() {
        return userVerifyId;
    }

    public void setUserVerifyId(String userVerifyId) {
        this.userVerifyId = userVerifyId;
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
        return "SelectUserVerifyResourceResponse{" +
                "userVerifyId='" + userVerifyId + '\'' +
                ", url='" + url + '\'' +
                ", position=" + position +
                '}';
    }
}
