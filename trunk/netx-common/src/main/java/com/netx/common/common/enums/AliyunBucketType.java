package com.netx.common.common.enums;

public enum AliyunBucketType {

    CreditBucket("credit"),//网信照片
    ActivityBucket("activity"),//活动、需求、技能、心愿照片
    ProductBucket("product"), //商家、商品图片空间
    UserBucket("ugc"); // 用户图片空间

    private String name;

    AliyunBucketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
