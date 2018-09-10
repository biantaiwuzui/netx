package com.netx.common.user.enums;

public enum NearlySettingEnum {
    NEARLY("显示我的信息", 0),
    NOTHING("不显示我的信息", 1);

    NearlySettingEnum(String name, Integer value){
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
