package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

public class MatchFlowDTO {
    @ApiModelProperty(value = "场次Id")
    private String venueId;
    @ApiModelProperty(value = "流程编号（第一个流程就传0，以此类推）")
    private Integer flowPath;
    @ApiModelProperty(value = "各个流程里的顺序(从0开始，以此类推)")
    private Integer flowPathSort;

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public Integer getFlowPath() {
        return flowPath;
    }

    public void setFlowPath(Integer flowPath) {
        this.flowPath = flowPath;
    }

    public Integer getFlowPathSort() {
        return flowPathSort;
    }

    public void setFlowPathSort(Integer flowPathSort) {
        this.flowPathSort = flowPathSort;
    }
}
