package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("工作经历详情信息")
public class InsertUserProfessionDetailRequest {

    @ApiModelProperty("id")
    private String id;

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "单位全称不能为空")
    @ApiModelProperty("单位全称")
    private String company;

    @NotBlank(message = "部门全称不能为空")
    @ApiModelProperty("部门全称")
    private String department;

    @NotBlank(message = "最高职位不能为空")
    @ApiModelProperty("最高职位")
    private String topProfession;

    //@NotBlank(message = "入职年份不能为空")
    @ApiModelProperty("入职年份")
    private String year;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
