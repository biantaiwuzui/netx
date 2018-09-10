package com.netx.common.common.enums;

public enum WorthTypeEnum {

    WISH_TYPE("Wish"),
    DEMAND_TYPE("Demand"),
    SKILL_TYPE("Skill"),
    MEETING_TYPE("Meeting"),
    MATCH_TYPE("Match"),
    MERCHANT_TYPE("Merchant"),
    PRODUCT_TYPE("Product"),
    //不想被查到后过滤的TYPE
    OTHER_TYPE("OTHER");


    private String name;

    WorthTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
