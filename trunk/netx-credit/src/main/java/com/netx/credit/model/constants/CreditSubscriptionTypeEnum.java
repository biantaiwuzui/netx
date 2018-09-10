package com.netx.credit.model.constants;

/**
 * @author lanyingchu
 * @date 2018/7/31 19:14
 */
public enum CreditSubscriptionTypeEnum {

    CASHIER {
        @Override
        public String getName() {
            return "收银人员"; // 受邀商家的收银人员
        }
    },
    MANAGER {
        @Override
        public String getName() {
            return "业务主管"; // 受邀商家的业务主管
        }
    },
    LEGAL {
        @Override
        public String getName() {
            return "法人代表"; // 受邀商家的法人代表
        }
    },
    REGISTER {
        @Override
        public String getName() {
            return "注册者"; // 受邀商家的注册者
        }
    },
    CUSTOMERSERVICE {
        @Override
        public String getName() {
            return "客服人员"; // 受邀商家的客服人员
        }
    },
    INNERFRIEND {
        @Override
        public String getName() {
            return "内购好友";
        }
    },
    FRIEND {
        @Override
        public String getName() {
            return "普通好友";
        }
    },
    NOMAL{
        @Override
        public String getName() {
            return "普通用户";
        }
    };

    public abstract String getName();
}
