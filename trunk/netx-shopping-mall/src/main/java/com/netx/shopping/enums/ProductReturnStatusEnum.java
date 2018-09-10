package com.netx.shopping.enums;

/**
 * Created By wj.liu
 * Description: 退货状态枚举
 * Date: 2017-09-14
 */
public enum ProductReturnStatusEnum {

    USER_APPLY(1, "用户申请退货"),
    SELLER_AGREE(2, "商家同意退货"),
    USER_CONFIRM(3, "用户确认退货"),
    USER_CANCEL(4, "用户撤销退货"),
    SUCCESS(5, "双方退货成功"),
    REFUSE(6, "商家拒收退货"),
    RETURN_APPEAL(7,"用户退货申诉");
    private Integer code;
    private String name;

    ProductReturnStatusEnum(Integer code, String name){
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
