package com.netx.common.user.enums;

public enum TicketTypeEnum {
    FREE("免费",1),
    PAY("付费",2);
    private String name;
    private Integer type;
    TicketTypeEnum(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
