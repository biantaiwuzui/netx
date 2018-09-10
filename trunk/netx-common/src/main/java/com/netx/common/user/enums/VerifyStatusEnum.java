package com.netx.common.user.enums;

/**
 * 认证状态
 * @author 李卓
 */
public enum VerifyStatusEnum {

    PENDING_AUTHENTICATION ("待认证", 0),
    PASSING_AUTHENTICATION ("通过认证", 1),
    REJECTIVE_AUTHENTICATION ("拒绝认证", 2);

    VerifyStatusEnum(String code, Integer value){
        this.name = code;
        this.value = value;
    }
    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
