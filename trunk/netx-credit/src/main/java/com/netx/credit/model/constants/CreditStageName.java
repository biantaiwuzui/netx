package com.netx.credit.model.constants;

/**
 * 网信认购阶段类别
 */
public enum CreditStageName {
    /**
     * 最高级
     */
    CSN_TOP {
        @Override
        public String getStageName() {
            return "CSN_TOP";
        }
        @Override
        public Double getSubscriptionRatio(){
            return 0.93;
        }
    },
    /**
     * 好友级
     */
    CSN_FRIEND {
        @Override
        public String getStageName() {
            return "CSN_FRIEND";
        }
        @Override
        public Double getSubscriptionRatio(){
            return 0.95;
        }
    },
    /**
     * 普通级
     */
    CSN_COMMON {
        @Override
        public String getStageName() {
            return "CSN_COMMON";
        }
        @Override
        public Double getSubscriptionRatio(){
            return 0.97;
        }
    };


    public abstract String getStageName();

    public abstract Double getSubscriptionRatio();
}
