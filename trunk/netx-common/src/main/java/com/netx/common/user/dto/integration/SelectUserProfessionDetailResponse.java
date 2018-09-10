package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户经历详情信息")
public class SelectUserProfessionDetailResponse {

    @ApiModelProperty("详情id")
    private String id;

    @ApiModelProperty("单位全称")
    private String company;

    @ApiModelProperty("部门全称")
    private String department;

    @ApiModelProperty("最高职业")
    private String topProfession;

    @ApiModelProperty("入职年份")
    private String year;

    @ApiModelProperty("位置（值越小，排序越优先）")
    private Integer position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTopProfession() {
        return topProfession;
    }

    public void setTopProfession(String topProfession) {
        this.topProfession = topProfession;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "SelectUserProfessionDetailResponse{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", department='" + department + '\'' +
                ", topProfession='" + topProfession + '\'' +
                ", year='" + year + '\'' +
                ", position=" + position +
                '}';
    }
}
