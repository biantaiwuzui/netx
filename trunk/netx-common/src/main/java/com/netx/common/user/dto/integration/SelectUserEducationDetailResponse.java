package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModelProperty;

public class SelectUserEducationDetailResponse {
    @ApiModelProperty("详情id")
    private String id;

    @ApiModelProperty("学校名称")
    private String school;

    @ApiModelProperty("院系名称")
    private String department;

    @ApiModelProperty("专业名称")
    private String speciality;

    @ApiModelProperty("入学年份")
    private String year;

    @ApiModelProperty("学习年限")
    private String time;

    @ApiModelProperty("所获学位")
    private String degree;

    @ApiModelProperty("位置序号（越小，越在最前面）")
    private Integer position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "SelectUserEducationDetailResponse{" +
                "id='" + id + '\'' +
                ", school='" + school + '\'' +
                ", department='" + department + '\'' +
                ", speciality='" + speciality + '\'' +
                ", year='" + year + '\'' +
                ", time='" + time + '\'' +
                ", degree='" + degree + '\'' +
                ", position=" + position +
                '}';
    }
}
