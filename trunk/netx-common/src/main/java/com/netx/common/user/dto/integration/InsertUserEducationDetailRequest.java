package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("文化教育详情信息")
public class InsertUserEducationDetailRequest {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "学校名称不能为空")
    @ApiModelProperty("学校名称")
    private String school;

    @NotBlank(message = "院系名称不能为空")
    @ApiModelProperty("院系名称")
    private String department;

    @NotBlank(message = "专业名称不能为空")
    @ApiModelProperty("专业名称")
    private String speciality;

    //@NotBlank(message = "入学年份不能为空")
    @ApiModelProperty("入学年份")
    private String year;

    //@NotBlank(message = "学习年限不能为空")
    @ApiModelProperty("学习年限")
    private String time;

    //@NotBlank(message = "所获学位不能为空")
    @ApiModelProperty("所获学位")
    private String degree;

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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "InsertUserEducationDetailRequest{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", school='" + school + '\'' + ", department='" + department + '\'' + ", speciality='" + speciality + '\'' + ", year='" + year + '\'' + ", time='" + time + '\'' + ", degree='" + degree + '\'' + '}';
    }
}
