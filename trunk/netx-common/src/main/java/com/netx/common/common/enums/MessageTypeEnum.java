package com.netx.common.common.enums;

public enum MessageTypeEnum {

    ACTIVITY_TYPE("Activity"),//网能【心愿、技能、活动、需求、比赛】
    USER_TYPE("User"),//网友
    PRODUCT_TYPE("Product"),//网商【商家、商品、订单、红包】
    CREDIT_TYPE("Credit");//网信

    private String  name;

    MessageTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
