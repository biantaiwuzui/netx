package com.netx.shopping.model.ordercenter.constants;

/**
 * Created by czou on 5/2/2018.
 */
public enum ShippingStatus {
    SS_UNSHIPPED {
        @Override
        public int order() {
            return 1;
        }

        @Override
        public String getUserDesc() {
            return "未发货";
        }

        @Override
        public String getMerchantDesc() {
            return "未发货";
        }
    },
    SS_SHIPPING {
        @Override
        public int order() {
            return 2;
        }

        @Override
        public String getUserDesc() {
            return "已发货";
        }

        @Override
        public String getMerchantDesc() {
            return "物流中";
        }
    },
    SS_RECEIVED {
        @Override
        public int order() {
            return 3;
        }

        @Override
        public String getUserDesc() {
            return "已收货";
        }

        @Override
        public String getMerchantDesc() {
            return "已收货";
        }
    },
    SS_RETURN{
        @Override
        public int order() {
            return 4;
        }

        @Override
        public String getUserDesc() {
            return "退货中";
        }

        @Override
        public String getMerchantDesc() {
            return "待收货";
        }
    } ;


    public abstract int order();

    public abstract String getUserDesc();

    public abstract String getMerchantDesc();
}
