package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

public class SellerStatusRequestDto {
    @ApiModelProperty("商家id")
    private String id;
    @ApiModelProperty("解除原因")
    private String overReason;
    @ApiModelProperty("拉黑原因")
    private String backReason;
    @ApiModelProperty("状态 1：白名单，2：黑名单")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOverReason() {
        return overReason;
    }

    public void setOverReason(String overReason) {
        this.overReason = overReason;
    }

    public String getBackReason() {
        return backReason;
    }

    public void setBackReason(String backReason) {
        this.backReason = backReason;
    }
}
