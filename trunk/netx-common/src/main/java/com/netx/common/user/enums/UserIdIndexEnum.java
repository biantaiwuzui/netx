package com.netx.common.user.enums;

/**
 * 用于 aop 的 CalculateProfileScore 注解
 * 准确找到存储 userId 的入参的位置
 * @author 李卓
 */
public enum UserIdIndexEnum {

    OBJECT("userId数据在vo/dto/po入参里面"),
    STRING("userId为单独字符串入参");

    UserIdIndexEnum(String name){
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
