package com.netx.common.common.enums;

public enum ActivateCodePrefixEnum {
    MATCH_TM("TM","比赛团队邀请码前缀"),
    MATCH_WM("WM","比赛邀请工作人员前缀");
    private String value;
    private String name;

    ActivateCodePrefixEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
