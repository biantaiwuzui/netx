package com.netx.searchengine.query;

/**
 * 资讯ES查询参数类
 * @Author hj.Mao
 */
public class ArticleSearchQuery extends BaseSearchQuery{

    /**
     * 是否封禁 1.被封禁住的   0.正常的（没有被封禁的）
     */
    private Integer isLock;

    /**
     * 资讯类型：
     1：图文
     2：音视
     */
    private Integer articleType;

    /**
     * 软文类型：
     0：普通咨讯（不是软文）
     1：正常软文
     2：违规软文
     */
    private Integer advertorialType;

    /**
     * 状态码：
     0：正常
     1：异常
     2：待审核
     3：待交押金
     4：押金余额不足
     */
    private Integer statusCode;
    /**
     * 标签筛选id
     */
    private String tagName;

    /**
     * 用户id
     */
    private String userId;

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public Integer getAdvertorialType() {
        return advertorialType;
    }

    public void setAdvertorialType(Integer advertorialType) {
        this.advertorialType = advertorialType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
