package com.netx.worth.vo;

import com.netx.worth.model.MatchRequirementData;

import java.util.List;

public class MatchParticipantDescVo {
    private MatchParticipantAllVo matchParticipantAllVo;
    private List<MatchRequirementData> matchRequirementDataList;

    public MatchParticipantAllVo getMatchParticipantAllVo() {
        return matchParticipantAllVo;
    }

    public void setMatchParticipantAllVo(MatchParticipantAllVo matchParticipantAllVo) {
        this.matchParticipantAllVo = matchParticipantAllVo;
    }

    public List<MatchRequirementData> getMatchRequirementDataList() {
        return matchRequirementDataList;
    }

    public void setMatchRequirementDataList(List<MatchRequirementData> matchRequirementDataList) {
        this.matchRequirementDataList = matchRequirementDataList;
    }
}
