package com.netx.worth.enums;

/**
 * 举办方、协办方、啥啥啥枚举
 * Created by Yawn on 2018/8/7 0007.
 */
public enum  MatchOrganizerKind {
    ORGANIZER(0, "主办方"),
    CO_ORGANIZER(1, "协办方"),
    SPONSOR(2, "赞助方"),
    UNITED_ORGANIZER(3, "联合举办"),
    DIRECTOR(4, "指导单位"),
    SUPPORT(5, "支持方");

    private Integer status;
    private String description;

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    MatchOrganizerKind(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
