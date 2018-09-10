package com.netx.session.store;

/**
 * 缓存服务，用于缓存页面内容的以及页面对象
 *
 */
public interface CacheService {

    Object get(String key);

    void put(String key, Object value);

    /**
     * 可指定存活时间的缓存接口
     *
     * @param key
     * @param value
     * @param second
     */
    void put(String key, Object value, int second);

    void hSet(String key, String field, Object value);

    Object hGet(String key, String field);

    void remove(String key);

    void removeAll();
}
