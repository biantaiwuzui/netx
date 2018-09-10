package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户兴趣爱好的详情信息")
public class SelectUserInterestDetailResponse {
    @ApiModelProperty("详情id")
    private String id;

    @ApiModelProperty("兴趣类别")
    private String interestType;

    @ApiModelProperty("具体内容")
    private String interestDetail;

    @ApiModelProperty("位置（值越小，排序越优先）")
    private Integer position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getInterestDetail() {
        return interestDetail;
    }

    public void setInterestDetail(String interestDetail) {
        this.interestDetail = interestDetail;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "SelectUserInterestDetailResponse{" +
                "id='" + id + '\'' +
                ", interestType='" + interestType + '\'' +
                ", interestDetail='" + interestDetail + '\'' +
                ", position=" + position +
                '}';
    }
}
