package com.netx.shopping.model.ordercenter.constants;

/**
 * Created by czou on 5/2/2018.
 */
public enum OrderStatusEnum {
    OS_ORDER{
        @Override
        public int order() {
            return 1;
        }

        @Override
        public String getUserDesc() {
            return "待付款";
        }

        @Override
        public String getMerchantDesc() {
            return "待用户付款";
        }
    },
    OS_CONFIRMED {
        @Override
        public int order() {
            return 2;
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
    OS_SHIPPING {
        @Override
        public int order() {
            return 3;
        }

        @Override
        public String getUserDesc() {
            return "物流中";
        }

        @Override
        public String getMerchantDesc() {
            return "物流中";
        }
    },
    OS_RETURN {
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
            return "是否接受退货";
        }
    },
    OS_COMPLATING {
        @Override
        public int order() {
            return 5;
        }

        @Override
        public String getUserDesc() {
            return "投诉中";
        }

        @Override
        public String getMerchantDesc() {
            return "被投诉中";
        }
    },
    OS_COMMENT {
        @Override
        public int order() {
            return 6;
        }

        @Override
        public String getUserDesc() {
            return "待评论";
        }

        @Override
        public String getMerchantDesc() {
            return "待买家评论";
        }
    },
    OS_FINISH {
        @Override
        public int order() {
            return 7;
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
    OS_CANCELED {
        @Override
        public int order() {
            return 8;
        }

        @Override
        public String getUserDesc() {
            return "交易取消";
        }

        @Override
        public String getMerchantDesc() {
            return "交易取消";
        }
    },
    OS_WATTING {
        @Override
        public int order() {
            return 9;
        }

        @Override
        public String getUserDesc() {
            return "待生成";
        }

        @Override
        public String getMerchantDesc() {
            return "待生成";
        }
    },
    OS_FINSH_PAY {
        @Override
        public int order() {
            return 10;
        }

        @Override
        public String getUserDesc() {
            return "已付款";
        }

        @Override
        public String getMerchantDesc() {
            return "已付款";
        }
    },
    OS_SERVICE {
        @Override
        public int order() {
            return 11;
        }

        @Override
        public String getUserDesc() {
            return "商家已服务";
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
