package com.netx.shopping.model.merchantcenter.constants;

public enum  MerchantManagerEnum {

    CASHIER{
        @Override
        public String getName() {
            return "收银人员";
        }
    },
    MANAGER{
        @Override
        public String getName() {
            return "业务主管";
        }
    },
    LEGAL{
        @Override
        public String getName() {
            return "法人代表";
        }
    },
    REGISTER{
        @Override
        public String getName() {
            return "注册者";
        }
    },
    PRIVILEGE{
        @Override
        public String getName() {
            return "特权行使人";
        }
    },
    CUSTOMERSERVICE{
        @Override
        public String getName() {
            return "客服人员";
        }
    };

    public abstract String getName();
}
