package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 评分VO
 * Created by Yawn on 2018/8/20 0020.
 */
public class MatchScoreVo {
    @ApiModelProperty("评分者名字")
    private String raterName;
    @ApiModelProperty("分数")
    private Double score;

    public String getRaterName() {
        return raterName;
    }

    public void setRaterName(String raterName) {
        this.raterName = raterName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
