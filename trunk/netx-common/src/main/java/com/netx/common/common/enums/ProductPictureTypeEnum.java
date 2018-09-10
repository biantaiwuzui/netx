package com.netx.common.common.enums;

public enum ProductPictureTypeEnum {
    //普通照片
    NONE{
        @Override
        public String getValue() {
            return "none";
        }
    },
    //详情照片
    DETAIL{
        @Override
        public String getValue() {
            return "detail";
        }
    };

    public abstract String getValue();
}
