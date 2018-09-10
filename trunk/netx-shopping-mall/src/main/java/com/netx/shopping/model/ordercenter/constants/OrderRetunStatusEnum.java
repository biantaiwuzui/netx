package com.netx.shopping.model.ordercenter.constants;

public enum OrderRetunStatusEnum {

    ORS_USER_APPLY{
        @Override
        public int status() {
            return 1;
        }

        @Override
        public String getName() {
            return "用户申请退货";
        }
    },
    ORS_MERCHANT_AGREE{
        @Override
        public int status() {
            return 2;
        }

        @Override
        public String getName() {
            return "商家同意退货";
        }
    },
    ORS_USER_AGREE{
        @Override
        public int status() {
            return 3;
        }

        @Override
        public String getName() {
            return "用户确认退货";
        }
    },
    ORS_USER_CANCEL{
        @Override
        public int status() {
            return 4;
        }

        @Override
        public String getName() {
            return "用户撤销退货";
        }
    },
    ORS_FINISH{
        @Override
        public int status() {
            return 5;
        }

        @Override
        public String getName() {
            return "退货成功";
        }
    },
    ORS_MERCHANT_REJECT{
        @Override
        public int status() {
            return 6;
        }

        @Override
        public String getName() {
            return "商家拒收退货";
        }
    },
    ORS_USER_COPLAIN{
        @Override
        public int status() {
            return 7;
        }

        @Override
        public String getName() {
            return "用户退货申诉";
        }
    };

    public abstract int status();

    public abstract String getName();

}
