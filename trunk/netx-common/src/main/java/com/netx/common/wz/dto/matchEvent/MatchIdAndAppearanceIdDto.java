package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class MatchIdAndAppearanceIdDto {
    @ApiModelProperty("比赛id")
    @NotBlank(message = "比赛id不能为空")
    private String matchId;
    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String audienceId;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getAudienceId() {
        return audienceId;
    }

    public void setAudienceId(String audienceId) {
        this.audienceId = audienceId;
    }
}
