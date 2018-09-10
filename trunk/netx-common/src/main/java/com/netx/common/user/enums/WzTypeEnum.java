package com.netx.common.user.enums;

public enum WzTypeEnum {
    GIFT(1), // 礼物
    INVITATION(2);// 邀请
    WzTypeEnum(Integer value) {
        this.value = value;
    }
    private Integer value;
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
}