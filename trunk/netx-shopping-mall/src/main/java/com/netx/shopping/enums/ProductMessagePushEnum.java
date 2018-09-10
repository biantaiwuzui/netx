package com.netx.shopping.enums;

/**
 * Created By liwei
 * Description: 退货状态枚举
 * Date: 2017-11-03
 */
public enum ProductMessagePushEnum {

    REMIND_SELLER_SEND("提现商家发货","请尽快安排{0}订单号{1}发货，如订单达成后三日内未发货，订单将自动取消"),
    SELLER_REFUSE_SEND_1("商家拒绝收货","你退回给{0}商家的{0}商品被拒收，对方已申请仲裁，请尽快答辩。如在三日内未申诉，将裁决对方获胜，交易将正常结束，所有认购款将支付给对方"),
    SELLER_REFUSE_SEND_2("商家拒绝收货","你的退货订单{0}已被商家拒收，对方已申请仲裁，请尽快答辩。如在三日内未申诉，将裁决对方获胜，交易将正常结束，所有认购款将支付给对方");

    public String name;
    public String message;

    ProductMessagePushEnum(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
