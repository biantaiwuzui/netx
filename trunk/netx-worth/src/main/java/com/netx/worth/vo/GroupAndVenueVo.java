package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

public class GroupAndVenueVo {
    @ApiModelProperty(value = "赛组的名称")
    private String groupName;
    @ApiModelProperty(value = "赛组的编号")
    private String matchGroupId;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMatchGroupId() {
        return matchGroupId;
    }

    public void setMatchGroupId(String matchGroupId) {
        this.matchGroupId = matchGroupId;
    }
}
