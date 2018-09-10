package com.netx.common.user.enums;

public enum  SystemBlackStatusEnum {
    RELEASE("已释放（在白名单）或释放操作", 0),
    DEFRIEND("已拉黑（在黑名单）或拉黑操作", 1);

    SystemBlackStatusEnum(String name, Integer value){
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
