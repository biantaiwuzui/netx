package com.netx.common.wz.dto.matchEvent;

import com.netx.common.user.enums.TicketTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class MatchTicketPlanPayDto {
    @ApiModelProperty(value = "赛程id", required = true)
    @NotNull(message = "赛程id不能为空")
    private String matchId;
    @ApiModelProperty(value = "门票id", required = true)
    @NotNull(message = "门票id不能为空")
    private String matchTicketId;

    @ApiModelProperty(value = "门票类型1.FREE(免费)，2.PAY(付费)", required = true)
    @NotNull(message = "门票类型不能为空")
    private TicketTypeEnum ticketTypeEnum;

    public TicketTypeEnum getTicketTypeEnum() {
        return ticketTypeEnum;
    }

    public void setTicketTypeEnum(TicketTypeEnum ticketTypeEnum) {
        this.ticketTypeEnum = ticketTypeEnum;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchTicketId() {
        return matchTicketId;
    }

    public void setMatchTicketId(String matchTicketId) {
        this.matchTicketId = matchTicketId;
    }
}
