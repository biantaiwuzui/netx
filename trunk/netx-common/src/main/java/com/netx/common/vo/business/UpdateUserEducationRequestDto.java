package com.netx.common.vo.business;

import io.swagger.annotations.Api;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class UpdateUserEducationRequestDto {
@NotNull(message = "用户ID不能为空")
    private String id;
    /**
     * 学校名称
     */
    private String school;
    /**
     * 院系名称
     */
    private String department;
    /**
     * 专业名称
     */
    private String speciality;
    /**
     * 入学年份
     */
    private String year;
    /**
     * 学习年限
     */
    private String time;

    /**
     * 所获学位
     */
    private String degree;
    /**
     * 位置序号
     */
    private Integer position;

    private String updateUserId;

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

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}
