package com.netx.utils.cache;

public enum RedisTypeEnum {

    LIST_TYPE("list"),
    ZSET_TYPE("zset"),
    SET_TYPE("set"),
    STRING_TYPE("string"),
    OBJECT_TYPE("object"),
    HSET_TYPE("hset");

    private String name;

    RedisTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
