package com.netx.ucenter.model.user;

import io.swagger.annotations.ApiModelProperty;

public class queryArticleClickHistoryCountData {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("点赞数统计数量统计")
    private Integer sorce;

    public Integer getSorce() {
        return sorce;
    }

    public void setSorce(Integer sorce) {
        this.sorce = sorce;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
