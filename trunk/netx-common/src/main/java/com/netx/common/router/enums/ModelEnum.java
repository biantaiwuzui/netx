package com.netx.common.router.enums;

public enum ModelEnum {

    USER("user"),//用户模型
    USER_PROFILE("UserProfile"),//用户详情模型
    USER_PHOTO("UserPhoto");//用户照片模型

    ModelEnum(String model){
        this.model = model;
    }
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
