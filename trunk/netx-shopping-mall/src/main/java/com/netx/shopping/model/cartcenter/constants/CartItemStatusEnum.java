package com.netx.shopping.model.cartcenter.constants;

public enum CartItemStatusEnum {
    CIS_WATTING{
        @Override
        public int cartItem() {
            return 0;
        }

        @Override
        public String getName() {
            return "待结算";
        }
    },
    CIS_FINISH{
        @Override
        public int cartItem() {
            return 1;
        }

        @Override
        public String getName() {
            return "已结算";
        }
    };
    public abstract int cartItem();
    public abstract String getName();
}
