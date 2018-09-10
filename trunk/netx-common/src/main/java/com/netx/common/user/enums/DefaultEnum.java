package com.netx.common.user.enums;

/**
 * 默认枚举
 * 用于存储默认的值
 * 例如：公司的 toUserId(收钱 userId)，游客的伪 userId
 * @author 李卓
 */
public enum DefaultEnum {

    TO_COMPANY_WALLET("公司的 toUserId (收钱用户id)", "999");

    DefaultEnum(String name, Object value){
        this.name = name;
        this.value = value;
    }

    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
