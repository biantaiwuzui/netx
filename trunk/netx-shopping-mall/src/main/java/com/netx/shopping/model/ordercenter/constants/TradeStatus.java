package com.netx.shopping.model.ordercenter.constants;

/**
 * Created by czou on 5/2/2018.
 */
public enum TradeStatus {
    TS_WATTING{
        @Override
        public int trade(){
            return 1;
        }
        @Override
        public String getUserDesc(){
            return "待交易";
        }
        @Override
        public String getMerchantDesc(){
            return "待交易";
        }
    },
    TS_TRADED{
        @Override
        public int trade(){
            return 2;
        }
        @Override
        public String getUserDesc(){
            return "交易中";
        }
        @Override
        public String getMerchantDesc(){
            return "交易中";
        }
    },
    TS_SUCCESS{
        @Override
        public int trade(){
            return 3;
        }
        @Override
        public String getUserDesc(){
            return "交易成功";
        }
        @Override
        public String getMerchantDesc(){
            return "交易成功";
        }
    },
    TS_CANCELED{
        @Override
        public int trade(){
            return 4;
        }
        @Override
        public String getUserDesc(){
            return "交易取消";
        }
        @Override
        public String getMerchantDesc(){
            return "交易取消";
        }
    };

    public abstract int trade();

    public abstract String getUserDesc();

    public abstract String getMerchantDesc();
}
