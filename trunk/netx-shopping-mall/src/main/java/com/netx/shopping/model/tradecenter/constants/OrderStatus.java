package com.netx.shopping.model.tradecenter.constants;

/**
 * Created by czou on 5/2/2018.
 */
public enum OrderStatus {
    OS_CONFIRMED {
        @Override
        public int order() {
            return 1;
        }

        @Override
        public String getUserDesc() {
            return "待发货";
        }

        @Override
        public String getMerchantDesc() {
            return "待确认";
        }
    },
    OS_CANCELED {
        @Override
        public int order() {
            return 2;
        }

        @Override
        public String getUserDesc() {
            return "已取消";
        }

        @Override
        public String getMerchantDesc() {
            return "已取消";
        }
    },
    OS_VERIFIED {
        @Override
        public int order() {
            return 3;
        }

        @Override
        public String getUserDesc() {
            return "待发货";
        }

        @Override
        public String getMerchantDesc() {
            return "已审核";
        }
    },
    OS_FINISH {
        @Override
        public int order() {
            return 4;
        }

        @Override
        public String getUserDesc() {
            return "交易完成";
        }

        @Override
        public String getMerchantDesc() {
            return "交易完成";
        }
    },
    OS_INVALID {
        @Override
        public int order() {
            return 13;
        }

        @Override
        public String getUserDesc() {
            return "无效订单";
        }

        @Override
        public String getMerchantDesc() {
            return "无效订单";
        }
    },
    OS_CLOSED {
        @Override
        public int order() {
            return 0;
        }

        @Override
        public String getUserDesc() {
            return "交易关闭";
        }

        @Override
        public String getMerchantDesc() {
            return "交易关闭";
        }
    },
    OS_TEMP_STATUS {
        @Override
        public int order() {
            return 8;
        }

        @Override
        public String getUserDesc() {
            return "";
        }

        @Override
        public String getMerchantDesc() {
            return "";
        }
    };

    public abstract int order();

    public abstract String getUserDesc();

    public abstract String getMerchantDesc();
}
