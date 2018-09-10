package com.netx.common.user.enums;

public enum VerifyCreditEnum {
    USERIDCREDIT_TYPE("user_id"),
    IDNUMBERCREDIT_TYPE("id_number");
    private String name;

    VerifyCreditEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
