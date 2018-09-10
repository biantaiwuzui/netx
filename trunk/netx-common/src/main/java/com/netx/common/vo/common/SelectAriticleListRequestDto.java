package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @Author haojun
 * create by 14-9-2017
 */
@ApiModel(value = "资讯信息查询请求表")
public class SelectAriticleListRequestDto {

    @ApiModelProperty(value = "作者网号")
    private String userNumber;

    @ApiModelProperty(value = "咨询类别,资讯类型：1：图文  2：音视")
    private Integer articleType;

    @ApiModelProperty(value = "咨询标题")
    private String title;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
