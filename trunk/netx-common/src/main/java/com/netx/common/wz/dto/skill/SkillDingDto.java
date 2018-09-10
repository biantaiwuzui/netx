package com.netx.common.wz.dto.skill;

import java.math.BigDecimal;

public class SkillDingDto {
    private  String skillRegisterId;
    private BigDecimal bail;
    private String userId;

    public String getSkillRegisterId() {
        return skillRegisterId;
    }

    public void setSkillRegisterId(String skillRegisterId) {
        this.skillRegisterId = skillRegisterId;
    }

    public BigDecimal getBail() {
        return bail;
    }

    public void setBail(BigDecimal bail) {
        this.bail = bail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
