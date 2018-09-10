package com.netx.common.user.enums;


public enum PayTypeEnum {

    PT_NONE{
        @Override
        public int payType() {
            return 1;
        }
        @Override
        public String getName() {
            return "零钱支付";
        }

        @Override
        public String getTradeType() {
            return "0";
        }
    },
    PT_WECHAT{
        @Override
        public int payType() {
            return 2;
        }
        @Override
        public String getName() {
            return "微信支付";
        }
        @Override
        public String getTradeType() {
            return "0";
        }
    },
    PT_ALI{
        @Override
        public int payType() {
            return 3;
        }
        @Override
        public String getName() {
            return "支付宝支付";
        }
        @Override
        public String getTradeType() {
            return "0";
        }
    };

    public abstract int payType();

    public abstract String getName();

    public abstract String getTradeType();

}

