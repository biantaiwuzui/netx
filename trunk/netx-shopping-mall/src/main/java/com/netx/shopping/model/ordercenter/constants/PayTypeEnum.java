package com.netx.shopping.model.ordercenter.constants;

public enum PayTypeEnum {

    PT_NONE{
        @Override
        public int payType() {
            return 0;
        }

        @Override
        public int getTradeType() {
            return 0;
        }

        @Override
        public String getName() {
            return "零钱支付";
        }
    },
    PT_WECHAT{
        @Override
        public int payType() {
            return 1;
        }
        @Override
        public int getTradeType() {
            return 0;
        }
        @Override
        public String getName() {
            return "微信支付";
        }
    },
    PT_ALI{
        @Override
        public int payType() {
            return 2;
        }
        @Override
        public int getTradeType() {
            return 0;
        }
        @Override
        public String getName() {
            return "支付宝支付";
        }
    },
    PT_CREDIT{
        @Override
        public int payType() {
            return 3;
        }
        @Override
        public int getTradeType() {
            return 1;
        }
        @Override
        public String getName() {
            return "网信支付";
        }
    },
    PT_FUSE{
        @Override
        public int payType() {
            return 4;
        }
        @Override
        public int getTradeType() {
            return 2;
        }
        @Override
        public String getName() {
            return "零钱与网信混合支付";
        }
    },
    PT_WECHAT_FUSE{
        @Override
        public int payType() {
            return 5;
        }
        @Override
        public int getTradeType() {
            return 2;
        }
        @Override
        public String getName() {
            return "微信与网信混合支付";
        }
    },
    PT_ALI_FUSE{
        @Override
        public int payType() {
            return 6;
        }
        @Override
        public int getTradeType() {
            return 2;
        }
        @Override
        public String getName() {
            return "支付宝与网信混合支付";
        }
    };


    public abstract int payType();

    public abstract String getName();

    public abstract int getTradeType();
}
