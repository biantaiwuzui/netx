package com.netx.common.common.enums;

/**
 * redis浅醉枚举类，Key的话前面用KEY,HKey的话HKEY
 */
public enum RedisPrefixEnum {

    KEY_MATCH_TEAM_MEMBER_INTIVE("MATCH_TEAM_MEMBER_INTIVE","比赛邀请团队成员（用于存放邀请码）");
    private String value;
    private String name;

    RedisPrefixEnum(String value, String name) {
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
