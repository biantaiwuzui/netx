package com.netx.common.wz.dto.matchEvent;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;

public class MatchAwardDTO {
    @ApiModelProperty(value = "有id值情况表示更新，没有的话表示插入")
    private String id;
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    /**
     * 奖项名字
     */
    @ApiModelProperty(value = "奖项名字")
    private String awardName;
    @ApiModelProperty(value = "奖项名额")
    private Integer awardNumber;
    /**
     * 奖金
     */
    @ApiModelProperty(value = "奖金")
    private BigDecimal bonus;
    /**
     * 奖品
     */
    @ApiModelProperty(value = "奖品")
    private String prize;
    /**
     * 证书
     */
    @ApiModelProperty(value = "证书")
    private String certificate;
    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public Integer getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(Integer awardNumber) {
        this.awardNumber = awardNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
