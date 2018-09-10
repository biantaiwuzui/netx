package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 资讯待审核dto类
 * @Author hj.Mao
 * @Date 2017-11-18
 */
public class ArticleLockListResponseDto  extends UserInfomationResponseVo{

    @ApiModelProperty("资讯Id")
    private String articleId;

    @ApiModelProperty("资讯标题")
    private String title;

    @ApiModelProperty("资讯发布者发布的时间,时间字符串返回")
    private String date;

    @ApiModelProperty("点击数")
    private Long hits;

    @ApiModelProperty("押金")
    private BigDecimal amount;

    public String getArticleId() { return articleId; }

    public void setArticleId(String articleId) { this.articleId = articleId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
