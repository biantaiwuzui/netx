package com.netx.common.router.enums;

/**
 * 查询条件枚举
 * 根据什么查询，就填用什么枚举值
 */
public enum SelectConditionEnum{
    USER_ID("用户id", "id"),
    USER_NUMBER("网号", "user_number"),
    MOBILE("手机号", "mobile");

    SelectConditionEnum(String name, String value){
        this.name = name;
        this.value = value;
    }
    private String name;
    private String value;

    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
}
