package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class MatchPassReviewDto {
    @ApiModelProperty("赛事id")
    private String matchId;
    @ApiModelProperty("是否通过")
    private boolean isPass;
    @ApiModelProperty("不通过理由")
    private String reason;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
