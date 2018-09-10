package com.netx.common.vo.business;

import javax.validation.constraints.NotNull;

public class EditUserWorkExperienceRequestDto {

    private String company;

    private String department;

    private String topProfession;

    private String year;

    private String updateUserId;

    @NotNull(message = "信息id不能为空")
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}
