package com.netx.worth.enums;

public enum  MatchTimeTypeEnum {
    APPLYMATCH(0,"报名的默认时间"),
    MATCHTICKET(1,"购票的默认时间");
    private Integer type;
    private String name;

    MatchTimeTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }
}
