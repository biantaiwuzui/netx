package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("用户兴趣爱好信息")
public class SelectUserInterestResponse {

    @ApiModelProperty("概述")
    private String interestLabel;

    @ApiModelProperty("详情列表")
    private List<SelectUserInterestDetailResponse> list;

    public String getInterestLabel() {
        return interestLabel;
    }

    public void setInterestLabel(String interestLabel) {
        this.interestLabel = interestLabel;
    }

    public List<SelectUserInterestDetailResponse> getList() {
        return list;
    }

    public void setList(List<SelectUserInterestDetailResponse> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SelectUserInterestResponse{" +
                "interestLabel='" + interestLabel + '\'' +
                ", list=" + list +
                '}';
    }
}
