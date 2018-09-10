package com.netx.common.common.enums;

/**
 * Created by 85169 on 2017/12/5.
 */
public enum BillTypeEnum {

    PLATFORM_BILL(0,"平台流水"),
    OPERATE_BILL(1,"经营流水");

    private Integer code;
    private String value;
    BillTypeEnum(Integer code,String value)
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

}
