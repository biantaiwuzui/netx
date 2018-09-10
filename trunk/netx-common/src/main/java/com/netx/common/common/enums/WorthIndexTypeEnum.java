package com.netx.common.common.enums;

public enum WorthIndexTypeEnum {
    Credit_Type("信用最高"),
    Hold_Credit_Type("支持网信"),
    Newly_Publish_Type("最新发布"),
    Watch_Type("最受关注"),
    Recommend_Type("精心推荐"),
    Shape_Happy_Type("齐享欢乐"),
    Only_You_Type("只需要你"),
    Supply_Type("供不应求"),
    Disconnection_Type("齐力断金"),
    For_You_Type("为你推荐"),
    Top_Match_Event("热门赛事");
    private String name;

    WorthIndexTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
