package com.netx.shopping.model.ordercenter.constants;

public enum OrderPutOffStatusEnum {

    OPOS_USER_APPLY{
        @Override
        public int status() {
            return 1;
        }

        @Override
        public String getName() {
            return "用户申请延期";
        }
    },
    OPOS_MERCHANT_AGREE{
        @Override
        public int status() {
            return 2;
        }

        @Override
        public String getName() {
            return "商家同意延期";
        }
    },
    OPOS_MERCHANT_REJECT{
        @Override
        public int status() {
            return 3;
        }

        @Override
        public String getName() {
            return "商家拒收延期";
        }
    },
    OPOS_USER_CANCEL{
        @Override
        public int status() {
            return 4;
        }

        @Override
        public String getName() {
            return "用户撤销延期";
        }
    };

    public abstract int status();

    public abstract String getName();
}
