package com.netx.common.common.enums;

public enum LimitEnum {

    LIMIT_RELEASE_CREDIT(1,"减少信用值"),
    LIMIT_MONTHLY_PUBLISH(2,"每月发布的资讯"),
    LIMIT_DAYLY_PUBLISH(3,"每天发布的资讯"),
    LIMIT_FORBID_DAYLY_PUBLISH(4,"禁止发布持续天数"),
    LIMIT_EVERY_PAY_PUBLISH(5,"付费发布资讯"),
    LIMIT_FORBID_PUBLISH(6,"禁止发布任何资讯"),
    LIMIT_BLACKLIST_PUSH(7,"拉入黑名单");

    private Integer code;
    private String value;

    LimitEnum(Integer code,String value)
    {
        this.code=code;
        this.value=value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static LimitEnum getEnumByCode(Integer code){
        switch(code){
            case 1:
                return LimitEnum.LIMIT_RELEASE_CREDIT;
            case 2:
                return LimitEnum.LIMIT_MONTHLY_PUBLISH;
            case 3:
                return LimitEnum.LIMIT_DAYLY_PUBLISH;
            case 4:
                return LimitEnum.LIMIT_FORBID_DAYLY_PUBLISH;
            case 5:
                return LimitEnum.LIMIT_EVERY_PAY_PUBLISH;
            case 6:
                return LimitEnum.LIMIT_FORBID_PUBLISH;
            default:
                return LimitEnum.LIMIT_BLACKLIST_PUSH;
        }
    }

}
