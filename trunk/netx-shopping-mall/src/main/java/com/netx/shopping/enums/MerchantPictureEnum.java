package com.netx.shopping.enums;

public enum MerchantPictureEnum {

    LOGO("logo", "标识图片"),
    MERCHANT("merchant", "商家照片"),
    CERTI("certi", "证件照片");

    private String type;
    private String name;

    MerchantPictureEnum(String type, String name){
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
