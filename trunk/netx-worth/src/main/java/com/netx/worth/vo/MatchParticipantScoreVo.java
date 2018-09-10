package com.netx.worth.vo;

import java.math.BigDecimal;

/**
 * 参赛者比分
 * Created by Yawn on 2018/8/20 0020.
 */
public class MatchParticipantScoreVo {
    private String id;
    private String participantName;
    private String headImage;
    private BigDecimal score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
