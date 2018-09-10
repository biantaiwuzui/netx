package com.netx.utils.cache;

import org.apache.commons.lang3.StringUtils;

public class RedisKeyName {

    private String name;

    private RedisTypeEnum redisTypeEnum;

    private String id;

    public RedisKeyName(String name, RedisTypeEnum redisTypeEnum, String id) {
        this.name = name;
        this.redisTypeEnum = redisTypeEnum;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserKey(){
        return getKey("user");
    }

    public String getFriendKey(){
        return getKey("friend");
    }

    public String getCommonKey(){
        return getKey("common");
    }

    public String getSellerKey(){
        return getKey("seller");
    }

    public String getProductKey(){
        return getKey("product");
    }

    public String getWishKey(){
        return getKey("wish");
    }

    public String getDemandKey(){
        return getKey("demand");
    }

    public String getSkillKey(){
        return getKey("skill");
    }

    public String getMeetingKey(){
        return getKey("meeting");
    }

    public String getCreditKey(){
        return getKey("credit");
    }

    public String getOrderKey(){
        return getKey("order");
    }

    private String getKey(String netx){
        String pre = netx+"_"+name;
        if(StringUtils.isNotBlank(id)){
            return pre+"_"+redisTypeEnum.getName()+":"+id;
        }else{
            return pre+":"+redisTypeEnum.getName();
        }

    }

}
