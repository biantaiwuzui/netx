package com.netx.common.common.enums;

/**
 * @Author haojun
 * 仲裁查询方式枚举类
 * create by 2017.9.30
 */

public enum ArbitrationQueryTypeEnum {
    ARBITRATION_QUERY_BY_MYUSERID(0,"用户userId自己查询"),
    ARBITRATION_QUERY_BY_NICKNAME(1,"管理员通过nickname查询"),
    ARBITRATION_QUERY_BY_INPUTUSERID(2,"管理员通过输入userId查询"),
    ARBITRATION_QUERY_BY_OPUSERID(3,"管理员通过opUserId查询");

    private Integer code;
    private String value;

    ArbitrationQueryTypeEnum(Integer code,String value){
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
