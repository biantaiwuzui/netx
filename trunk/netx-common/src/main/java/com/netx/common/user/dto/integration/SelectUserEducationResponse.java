package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("文化教育概况和详情")
public class SelectUserEducationResponse {

    @ApiModelProperty("概况")
    private String educationLabel;

    @ApiModelProperty("详情列表")
    private List<SelectUserEducationDetailResponse> list;

    public String getEducationLabel() {
        return educationLabel;
    }

    public void setEducationLabel(String educationLabel) {
        this.educationLabel = educationLabel;
    }

    public List<SelectUserEducationDetailResponse> getList() {
        return list;
    }

    public void setList(List<SelectUserEducationDetailResponse> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SelectEducationResponse{" +
                "educationLabel='" + educationLabel + '\'' +
                ", list=" + list +
                '}';
    }

}
