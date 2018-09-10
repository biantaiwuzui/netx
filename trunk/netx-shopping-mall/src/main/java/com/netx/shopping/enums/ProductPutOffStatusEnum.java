package com.netx.shopping.enums;

public enum ProductPutOffStatusEnum {

    USER_APPLY(1, "用户申请延迟"),
    SELLER_AGREE(2, "商家同意延迟"),
    REFUSE(3, "商家拒收延迟"),
    USER_CANCEL(4, "用户撤销延迟"),
    SUCCESS(5, "双方延期成功");
    private Integer code;
    private String name;

    ProductPutOffStatusEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
