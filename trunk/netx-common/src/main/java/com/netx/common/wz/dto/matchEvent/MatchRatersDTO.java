package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 评分操作
 * Created by Yawn on 2018/8/2 0002.
 */
public class MatchRatersDTO {
    /**
     * 评分者称呼
     */
    @ApiModelProperty(value = "评分者称呼")
    private String ratersName;
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    @NotNull(message = "请指定当前赛程")
    @ApiModelProperty(value = "赛程ID，初赛，决赛这些", notes = "不为空，前端指定")
    private String progressId;
    /**
     * 参赛者ID
     */
    @NotNull(message = "请选定参赛者")
    @ApiModelProperty(value = "参赛者ID")
    private String participantId;
    /**
     * 分数
     */
    @NotNull(message = "请评选分数")
//    @Max(message = "最高分数", value = 100)
    @ApiModelProperty(value = "分数")
    @Min(message = "请大于等于0分", value = 0)
    private BigDecimal score;

    public String getRatersName() {
        return ratersName;
    }

    public void setRatersName(String ratersName) {
        this.ratersName = ratersName;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
