package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("用户工作经历信息")
public class SelectUserProfessionResponse {

    @ApiModelProperty("职业")
    private String professionLabel;

    @ApiModelProperty("工作经历详情列表")
    private List<SelectUserProfessionDetailResponse> list;

    public String getProfessionLabel() {
        return professionLabel;
    }

    public void setProfessionLabel(String professionLabel) {
        this.professionLabel = professionLabel;
    }

    public List<SelectUserProfessionDetailResponse> getList() {
        return list;
    }

    public void setList(List<SelectUserProfessionDetailResponse> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SelectUserProfessionResponse{" +
                "professionLabel='" + professionLabel + '\'' +
                ", list=" + list +
                '}';
    }
}
