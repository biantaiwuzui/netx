package com.netx.common.user.dto.article;

import io.swagger.annotations.ApiModelProperty;


import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文章创建时间
 * </p>
 */
public class ArticleCreateTimeDto {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建文章时间列表")
    private List<ArticleCreateTimeDto> articleCreateTimeDtoList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ArticleCreateTimeDto> getArticleCreateTimeDtoList() {
        return articleCreateTimeDtoList;
    }

    public void setArticleCreateTimeDtoList(List<ArticleCreateTimeDto> articleCreateTimeDtoList) {
        this.articleCreateTimeDtoList = articleCreateTimeDtoList;
    }

    @Override
    public String toString() {
        return "ArticleCreateTimeDto{" +
                "userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", articleCreateTimeDtoList=" + articleCreateTimeDtoList +
                '}';
    }
}
