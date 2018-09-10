package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class MatchParticpantAllDto {
    @ApiModelProperty("付费人信息")
    private MatchParticipantDTO matchParticipantDTO;
    @ApiModelProperty("队友或者小朋友的信息")
    private List<MatchChildInfoDTO> matchChildInfoDTOList;

    public MatchParticipantDTO getMatchParticipantDTO() {
        return matchParticipantDTO;
    }

    public void setMatchParticipantDTO(MatchParticipantDTO matchParticipantDTO) {
        this.matchParticipantDTO = matchParticipantDTO;
    }

    public List<MatchChildInfoDTO> getMatchChildInfoDTOList() {
        return matchChildInfoDTOList;
    }

    public void setMatchChildInfoDTOList(List<MatchChildInfoDTO> matchChildInfoDTOList) {
        this.matchChildInfoDTOList = matchChildInfoDTOList;
    }
}
