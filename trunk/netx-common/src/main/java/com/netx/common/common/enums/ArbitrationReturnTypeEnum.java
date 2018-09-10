package com.netx.common.common.enums;

public enum ArbitrationReturnTypeEnum {
    ARBITRATION_RETURN_TYPE_ALL(0,"返回所有仲裁信息"),
    ARBITRATION_RETURN_TYPE_CANUPDATE(1,"返回可修改仲裁信息"),
    ARBITRATION_RETURN_TYPE_READONLY(2,"返回只读仲裁信息");

    private Integer code;
    private String value;

    ArbitrationReturnTypeEnum(Integer code,String value){
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
