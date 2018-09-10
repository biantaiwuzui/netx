package com.netx.common.user.enums;

/**
 * 用户角色
 * @author 李卓
 */
public enum UserRoleEnum {
    SYSTEM_MANAGEMENT("系统管理", 1),
    USER_MANAGEMENT("用户管理", 2),
    BUSINESS_MANAGEMENT("商家管理", 3),
    MESSAGE_MANAGEMENT("资讯管理", 4),
    FINANCE_MANAGEMENT("财务管理", 5),
    ARBITRATION_MANAGEMENT("仲裁管理", 6);

    UserRoleEnum(String name, Integer value){
        this.name = name;
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
