package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class MatchTicketAndDefaultTimeDTO {
    @ApiModelProperty("默认档次")
    List<MatchTicketDTO> matchTicketDTOList;
    @ApiModelProperty("默认时间（不是默认的不填这个）")
    MatchApplyDefaultTimeDto matchApplyDefaultTimeDto;

    public List<MatchTicketDTO> getMatchTicketDTOList() {
        return matchTicketDTOList;
    }

    public void setMatchTicketDTOList(List<MatchTicketDTO> matchTicketDTOList) {
        this.matchTicketDTOList = matchTicketDTOList;
    }

    public MatchApplyDefaultTimeDto getMatchApplyDefaultTimeDto() {
        return matchApplyDefaultTimeDto;
    }

    public void setMatchApplyDefaultTimeDto(MatchApplyDefaultTimeDto matchApplyDefaultTimeDto) {
        this.matchApplyDefaultTimeDto = matchApplyDefaultTimeDto;
    }
}
