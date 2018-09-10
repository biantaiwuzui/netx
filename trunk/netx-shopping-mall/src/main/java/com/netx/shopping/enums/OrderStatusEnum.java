package com.netx.shopping.enums;

/**
 * Created By wj.liu
 * Description: 订单状态枚举
 * Date: 2017-09-15
 */
public enum OrderStatusEnum {

    GOUWUCHE(0,"购物车"),
    UNPAID(1, "待付款"),
    UNSEND(2, "待发货"),
    ONWAY(3, "物流中"),
    INRETURN(4, "退货中"),
    COMPLAINTS(5, "投诉中"),
    TOEVALUAT(6, "待评论"),
    COMPLETED(7, "已完成"),
    CANCELED(8, "已取消"),
    //网值活动订单状态  活动成功发行前是待生成,活动成功发行是已付款，商家确认服务是已服务
    TOBEGENERATE(9,"待生成"),
    PAID(10,"已付款"),
    SERVED(11,"已服务"),
    PUTOFF(12,"已延迟");

    private int code;
    private String name;

    OrderStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
