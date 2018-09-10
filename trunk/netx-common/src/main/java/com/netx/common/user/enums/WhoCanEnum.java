package com.netx.common.user.enums;

public enum WhoCanEnum {

    ALL("所有人", 0),
    ALL_FRIENDS("所有好友", 1),
    WATCH_PEOPLE("我关注的人", 2),
    PEOPLE_WATCH("关注我的人", 3),
    DESIGNED_FRIENDS("指定好友（可多个）", 4);

    WhoCanEnum(String name, Integer value){
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
