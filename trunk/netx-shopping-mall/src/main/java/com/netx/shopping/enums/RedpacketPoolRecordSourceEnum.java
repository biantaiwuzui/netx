package com.netx.shopping.enums;

public enum  RedpacketPoolRecordSourceEnum {
    TRADE_AMOUNT_COMMISSION(1,"交易额的提成"),
    ADVERTISEMENT_AMOUNT(2,"图文及音视的广告点击费"),
    PUBLISH_CURRENCY_OVERFLOW(3,"网币发行的溢价部分（扣除平台计提的利润）"),
    ACTIVITY_COMMISSION(4,"从活动、需求、技能等收益中的提成"),
    WISH_BALANCE(5,"心愿款的余额部分"),
    LASTDAY_REDPACKET_START_AMOUNT(6,"上日的红包启动金额"),
    COMPLETE_BUY(7,"答题竞购"),
    REDPACKET_SEND(8,"红包发放");

   private int code;
   private String name;

    RedpacketPoolRecordSourceEnum(int code, String name) {
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
