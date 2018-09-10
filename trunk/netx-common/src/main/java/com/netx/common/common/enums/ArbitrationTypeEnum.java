package com.netx.common.common.enums;

public enum ArbitrationTypeEnum {
    ORDER(1,"订单投诉类型(订单相关)"),
    NORMAL(2,"普通普通类型(言行举止相关)");


    private Integer code;
    private String name;
    ArbitrationTypeEnum(Integer code,String name){
        this.code=code;
        this.name=name;
    }

    public Integer getCode() { return code; }

    public String getName() { return name; }

}
