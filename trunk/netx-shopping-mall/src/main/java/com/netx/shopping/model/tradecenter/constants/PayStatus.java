package com.netx.shopping.model.tradecenter.constants;

/**
 * Created by czou on 5/2/2018.
 */
public enum PayStatus {
    PS_UNPAID {
        @Override
        public String getUserDesc() {
            return "待付款";
        }

        @Override
        public String getMerchantDesc() {
            return "待付款";
        }
    },
    PS_PAYING {
        @Override
        public String getUserDesc() {
            return "支付确认中";
        }

        @Override
        public String getMerchantDesc() {
            return "支付确认中";
        }
    },
    PS_PAYED {
        @Override
        public String getUserDesc() {
            return "已付款";
        }

        @Override
        public String getMerchantDesc() {
            return "已付款";
        }
    };


    public abstract String getUserDesc();

    public abstract String getMerchantDesc();
}
