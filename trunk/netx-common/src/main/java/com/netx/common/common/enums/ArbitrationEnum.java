package com.netx.common.common.enums;

/**
 * 裁决的状态
 */
public enum ArbitrationEnum {

    ARBITRATION_DOESNT_SETTLE(1,"未受理"),
    ARBITRATION_SETTLING(2,"受理中"),
    ARBITRATION_OTHER_COMPLAINT(3,"已申诉"),
    ARBITRATION_SETTLED(4,"已裁决"),
    ARBITRATION_REFUSE_SETTLE(5,"拒绝受理");

    private Integer code;
    private String value;
    ArbitrationEnum(Integer code,String value)
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
