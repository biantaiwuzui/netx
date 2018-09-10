package com.netx.utils.cache;


public interface Cache {

    /**
    具有过期时间的缓存
    <br/>put与mapPut“可以”互相覆盖
    @param key 键
    @param value 值
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
     */
    void put(String key, Object value, int seconds);

    /**
    不过期的缓存
    <br/>put与mapPut“可以”互相覆盖
    @param key 键
    @param value 值
     */
    void put(String key, Object value);

    /**
    取得缓存结果，如果是通过mapPut进行的缓存，会将key对应的所有field-value作为一个ConcurrentHashMap返回
    @param key
    @return 
     */
    Object get(String key);

    /**
    删除一个缓存，如果是通过mapPut进行的缓存，会将key对应的所有field-value删除
    @param key 
     */
    void del(String key);

    /**
    具有过期时间的缓存<br/>
    将value缓存到一个Map中，在Map中的键为field，Map在缓存中的键为key
    <br/>put与mapPut“可以”互相覆盖
    @param key 缓存的键
    @param field map的键
    @param value 缓存的值
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
     */
    void hset(String key, String field, Object value, int seconds);

    /**
    不过期的缓存<br/>
    将value缓存到一个Map中，在Map中的键为field，Map在缓存中的键为key
    <br/>put与mapPut“可以”互相覆盖
    @param key 缓存的键
    @param field map的键
    @param value 缓存的值
     */
    void hset(String key, String field, Object value);

    /**
    取得key缓存的map中的field的value,如果key对应的缓存不存在或者不是map,会返回null
     */
    Object get(String key, String field);

    /**
    删除一个key缓存的map中的field，如果key对应的缓存不存在或者不是map，不做任何操作
    @param key 
     */
    void del(String key, String field);

    /**
    缓存中是否存在这个键，只有key不存在或过期（会删除）时才返回false。即便key存储的是null或者false，依然会返回true。
    @param key
    @return 
     */
    boolean exists(String key);

    /**
    map型缓存中是否存在这个键，key不存在或field不存在或过期（会删除）时才返回false。即便key-field中存储的是null或者false，依然会返回true。
    @param key
    @return 
     */
    boolean exists(String key, String field);

    /**
    原子给一个key的值加1，并返回增加后的值。
    如果这个key不存在，会将1作为key的value存储起来，并返回1。
    @return 
     */
    Long incrBy(String key);

    /**
    原子给一个key的值加increment，并返回增加后的值。
    如果这个key不存在，会将increment作为key的value存储起来，并返回increment。
    @param increment
    @return 
     */
    Long incrBy(String key, long increment);

    /**
    原子给一个key的值加1，并返回增加后的值。
    如果这个key不存在，会将1作为key的value存储起来，并返回1。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long incrBy(String key, int seconds);

    /**
    原子给一个key的值加increment，并返回增加后的值。
    如果这个key不存在，会将increment作为key的value存储起来，并返回increment。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long incrBy(String key, long increment, int seconds);

    /**
    原子给一个key的值减1，并返回减少后的值。
    如果这个key不存在，会将 -1 作为key的value存储起来，并返回 -1。
    @return 
     */
    Long decrBy(String key);

    /**
    原子给一个key的值减increment，并返回减少后的值。
    如果这个key不存在，会将 -increment 作为key的value存储起来，并返回 -increment。
    @return 
     */
    Long decrBy(String key, long increment);

    /**
    原子给一个key的值减1，并返回减少后的值。
    如果这个key不存在，会将-1作为key的value存储起来，并返回-1。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long decrBy(String key, int seconds);

    /**
    原子给一个key的值减increment，并返回减少后的值。
    如果这个key不存在，会将 -increment 作为key的value存储起来，并返回 -increment。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long decrBy(String key, long increment, int seconds);

    /**
    原子给一个key缓存的map中的field的值加1，并返回增加后的值。
    如果这个key缓存的map中的field不存在，会将1作为key缓存的map中的field的value存储起来，并返回1。
    @return 
     */
    Long incrBy(String key, String field);

    /**
    原子给一个key缓存的map中的field的值加increment，并返回增加后的值。
    如果这个key缓存的map中的field不存在，会将increment作为key缓存的map中的field的value存储起来，并返回increment。
    @return 
     */
    Long incrBy(String key, String field, long increment);

    /**
    原子给一个key缓存的map中的field的值加1，并返回增加后的值。
    如果这个key缓存的map中的field不存在，会将1作为key缓存的map中的field的value存储起来，并返回1。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long incrBy(String key, String field, int seconds);

    /**
    原子给一个key缓存的map中的field的值加increment，并返回增加后的值。
    如果这个key缓存的map中的field不存在，会将increment作为key缓存的map中的field的value存储起来，并返回increment。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long incrBy(String key, String field, long increment, int seconds);

    /**
    原子给一个key缓存的map中的field的值减1，并返回减少后的值。
    如果这个key缓存的map中的field不存在，会将 -1 作为key缓存的map中的field的value存储起来，并返回 -1。
    @return 
     */
    Long decrBy(String key, String field);

    /**
    原子给一个key缓存的map中的field的值减increment，并返回减少后的值。
    如果这个key缓存的map中的field不存在，会将 -increment 作为key缓存的map中的field的value存储起来，并返回 -increment。
    @return 
     */
    Long decrBy(String key, String field, long increment);

    /**
    原子给一个key缓存的map中的field的值减1，并返回减少后的值。
    如果这个key缓存的map中的field不存在，会将-1作为key缓存的map中的field的value存储起来，并返回-1。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long decrBy(String key, String field, int seconds);

    /**
    原子给一个key缓存的map中的field的值减increment，并返回减少后的值。
    如果这个key缓存的map中的field不存在，会将 -increment 作为key缓存的map中的field的value存储起来，并返回 -increment。
    @param seconds 过期时间,单位秒,如果小于或等于0,会抛出异常
    @return 
     */
    Long decrBy(String key, String field, long increment, int seconds);
}
