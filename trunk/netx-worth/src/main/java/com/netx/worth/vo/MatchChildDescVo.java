package com.netx.worth.vo;

import com.netx.worth.model.MatchChildInfo;
import com.netx.worth.model.MatchRequirementData;

import java.util.List;

public class MatchChildDescVo {
    private MatchChildInfo matchChildInfo;
    private List<MatchRequirementData> matchRequirementDataList;

    public MatchChildInfo getMatchChildInfo() {
        return matchChildInfo;
    }

    public void setMatchChildInfo(MatchChildInfo matchChildInfo) {
        this.matchChildInfo = matchChildInfo;
    }

    public List<MatchRequirementData> getMatchRequirementDataList() {
        return matchRequirementDataList;
    }

    public void setMatchRequirementDataList(List<MatchRequirementData> matchRequirementDataList) {
        this.matchRequirementDataList = matchRequirementDataList;
    }
}
