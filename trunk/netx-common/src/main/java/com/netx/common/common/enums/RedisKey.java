package com.netx.common.common.enums;

public class RedisKey {

    public static String getKey(Class className,String type,String id){
        return className.getName()+"_"+type+"_"+id;
    }
}
