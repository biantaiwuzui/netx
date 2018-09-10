package com.netx.common.redis.service;

/**
 * 对 redis 进行操作的接口
 * @author 黎子安
 * @date 2017/10/21.
 */
public interface RedisService {
    public Object getValueJsonObject(String key,Object object);

    public boolean insertValue(String key,String value,long time);

    public boolean reFreshValue(String key,long time);

    public boolean deleteValue(String key);
}
