package com.netx.shopping.model.productcenter.constants;

public enum CategoryIconEnum {

    RECEPTION{
        @Override
        public String getName() {
            return "前台类目";
        }
    },
    BACKSTAGE{
        @Override
        public String getName() {
            return "后台类目";
        }
    };

    public abstract String getName();
}
