package com.netx.shopping.model.ordercenter.constants;

/**
 * 订单类型
 * @author 黎子安
 * @since 2018-05-07
 */
public enum OrderTypeEnum {
    //正常订单
    NORMAL_ORDER{
        @Override
        public String getName() {
            return "normal_order";
        }
    },
    //需求订单
    DEMAND_ORDER{
        @Override
        public String getName() {
            return "emand_order";
        }
    },
    //活动订单
    ACTIVITY_ORDER{
        @Override
        public String getName() {
            return "activity_order";
        }
    };
    public abstract String getName();
}
