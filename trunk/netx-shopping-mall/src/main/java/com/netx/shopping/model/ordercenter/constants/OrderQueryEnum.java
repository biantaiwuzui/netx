package com.netx.shopping.model.ordercenter.constants;

public enum OrderQueryEnum {

    OQ_ALL{
        @Override
        public String getName() {
            return "全部";
        }

        @Override
        public String[] getStatus() {
            return new String[]{OrderStatusEnum.OS_ORDER.name(),OrderStatusEnum.OS_RETURN.name(),
                    OrderStatusEnum.OS_COMPLATING.name(),OrderStatusEnum.OS_COMMENT.name(),
                    OrderStatusEnum.OS_CONFIRMED.name(), OrderStatusEnum.OS_SHIPPING.name(),
                    OrderStatusEnum.OS_FINSH_PAY.name(), OrderStatusEnum.OS_SERVICE.name(),
                    OrderStatusEnum.OS_FINISH.name(), OrderStatusEnum.OS_CANCELED.name()};
        }
    },
    OQ_ORDER{
        @Override
        public String getName() {
            return "待付款";
        }

        @Override
        public String[] getStatus() {
            return new String[]{OrderStatusEnum.OS_ORDER.name()};
        }
    },
    OQ_CONFIRMED{
        @Override
        public String getName() {
            return "待发货";
        }

        @Override
        public String[] getStatus() {
            return new String[]{OrderStatusEnum.OS_CONFIRMED.name()};
        }
    },
    OQ_COMMENT{
        @Override
        public String getName() {
            return "待评价";
        }

        @Override
        public String[] getStatus() {
            return new String[]{OrderStatusEnum.OS_COMMENT.name()};
        }
    },
    OQ_SHIPPING{
        @Override
        public String getName() {
            return "待收货/消费";
        }

        @Override
        public String[] getStatus() {
            return new String[]{OrderStatusEnum.OS_SHIPPING.name()};
        }
    };

    public abstract String getName();

    public abstract String[] getStatus();
}
