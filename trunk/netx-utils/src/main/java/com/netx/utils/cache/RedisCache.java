package com.netx.utils.cache;


import com.netx.utils.common.SerializeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class RedisCache {
    private final static Log logger = LogFactory.getLog(RedisCache.class);

    private String cacheAddress = "127.0.0.1";

    private Integer cachePort = 6379;

    private String cachePassword = "";

    public String getCacheAddress() {
        return cacheAddress;
    }

    public void setCacheAddress(String cacheAddress) {
        this.cacheAddress = cacheAddress;
    }

    private JedisPool pool = null;

    private static RedisCache redisCache = null;

    public static RedisCache getRedisCache(String address, String password, Integer port) {
        if (redisCache == null) {
            redisCache = new RedisCache(address, password, port);
        }
        return redisCache;

    }

    public RedisCache(String address, String password, Integer port) {
        if (address != null) {
            this.cacheAddress = address;
        }
        if (password != null) {
            this.cachePassword = password;
        }
        if (port != null) {
            this.cachePort = port;
        }
        client();
    }

    private void client() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            //config.setMaxActive(-1);
            config.setMaxTotal(-1);
            // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(2000);
            // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            //config.setMaxWait(1000 * 30);
            config.setMaxWaitMillis(1000 * 30);
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);

            //String[] addr = cacheAddress.split(":");
            if (StringUtils.isEmpty(cachePassword)) {
                pool = new JedisPool(config, cacheAddress, cachePort, 1000 * 30);
            } else {
                pool = new JedisPool(config, cacheAddress, cachePort, 1000 * 30, cachePassword);
            }
            logger.info("pool:" + pool);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("不能初始化Redis客户端", e);
        }
    }

    public Object get(String key) {
        Jedis jedis = null;
        try {
            //获取一个连接实例
            jedis = pool.getResource();
            //System.out.println(pool.getResource());
            byte[] data = jedis.get(key.getBytes());
            if (data == null || data.length <= 0) {
                return null;
            }
            return SerializeUtil.unserialize(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }
    public String getString(String key) {
        Jedis jedis = null;
        try {
            //获取一个连接实例
            jedis = pool.getResource();
            String string=jedis.get(key);
            if(StringUtils.isNoneBlank(string)) {
                return string;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }
    public String watch(String ...key) {
        Jedis jedis = null;
        try {
            //获取一个连接实例
            jedis = pool.getResource();
            return jedis.watch(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public String getStat(String key) {
        Jedis jedis = null;
        try {
            //获取一个连接实例
            jedis = pool.getResource();
            byte[] data = jedis.get(key.getBytes());
            if (data == null || data.length <= 0) {
                return null;
            }
            return new String(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }


    public String getRaw(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void put(String key, Object value) {
        Jedis jedis = null;
        try {
            String name = value.getClass().getName();
            //  Method[] methods=value.getClass().getMethods();
            if (name.equals("com.netx.ucenter.model.user.User")) {
                Object realNameValue = value.getClass().getMethod("getRealName").invoke(value);
                Object nickNameValue = value.getClass().getMethod("getNickname").invoke(value);
                if (realNameValue == null && nickNameValue == null) {
                    throw new RuntimeException("错误的数据写入到Redis" + new Date());
                }
            }

            jedis = pool.getResource();
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void put(String key, Object value, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            jedis.expire(key.getBytes(), second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void put(String key, String value, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            jedis.expire(key, second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void put(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void hSet(String key, String field, Object value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Object hGet(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] data = jedis.hget(key.getBytes(), field.getBytes());
            if (data == null || data.length <= 0) {
                return null;
            }
            return SerializeUtil.unserialize(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Object hGetRaw(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Object obj = jedis.hget(key, field);
            return obj;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long hSetRaw(String key, String field, String val) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hset(key, field, val);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void remove(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long remove(String[] keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(keys);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void removeAll() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.flushAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hkeys(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void hdel(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * 删除key
     * @param key
     */
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }
    public void hSet(String key, String field, Object value, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
            jedis.expire(key.getBytes(), second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }


    public void lpush(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void rpush(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.rpush(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void lpush(String key, Object value, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key.getBytes(), SerializeUtil.serialize(value));
            jedis.expire(key.getBytes(), second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Object lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] data = jedis.lpop(key.getBytes());
            if (data == null) return null;
            return SerializeUtil.unserialize(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void lset(String key, long index, Object value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String result = jedis.lset(key.getBytes(), index, SerializeUtil.serialize(value));
            logger.info(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public List<Object> lRange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            List<byte[]> list = jedis.lrange(key.getBytes(), start, end);
            List<Object> result = new ArrayList<>();
            if (list != null && list.size() > 0) {
                list.forEach(data -> {
                    result.add(SerializeUtil.unserialize(data));
                });
            }
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * 删除列表中的某个值
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lRem(String key, long count, Object value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrem(key.getBytes(), count, SerializeUtil.serialize(value));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void zaddOne(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key, score, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void zaddOne(String key, double score, String member, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key, score, member);
            jedis.expire(key, second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /*public void zadd(String key,Map<Double,String> members){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key,members);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }*/
    public void zadd(String key, Map<String, Double> members) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    //    public void zaddStat(String key,Integer count,String UserInfo){
//        Jedis jedis = null;
//        try {
//            jedis = pool.getResource();
//            jedis.zadd(key,count,UserInfo);
//           // jedis.zadd("1",1);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            pool.returnBrokenResource(jedis);
//            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
//        } finally {
//            pool.returnResource(jedis);
//        }
//    }
/*
    public void zadd(String key, Map<Double, String> members, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key,members);
            jedis.expire(key,second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }*/
    public void zadd(String key, Map<String, Double> members, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key, members);
            jedis.expire(key, second);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public double zincrby(String key, String member, double second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zincrby(key, second, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void zrem(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zrem(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long zrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrank(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public long zunionstore(String dstKey, String[] keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zunionstore(dstKey, keys);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public long zunionstore(String dstKey, String[] keys, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            long count = jedis.zunionstore(dstKey, keys);
            jedis.expire(dstKey, second);
            return count;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Boolean exists = jedis.exists(key.getBytes());
            if (exists != null) {
                return exists;
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long incrBy(String key, long increment) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrBy(key.getBytes(), increment);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long decrBy(String key, long decrement) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key.getBytes(), decrement);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long incrBy(String key, long increment, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            long result = jedis.incrBy(key.getBytes(), increment);
            jedis.expire(key.getBytes(), second);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long hincrBy(String key, String field, long num) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrBy(key.getBytes(), field.getBytes(), num);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long hincrBy(String key, String field, long num, int second) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            long result = jedis.hincrBy(key.getBytes(), field.getBytes(), num);
            jedis.expire(key.getBytes(), second);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(pattern);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
            throw new RuntimeException("Redis出现错误！" + e.getMessage(), e);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    // 加锁标志
    private static final String LOCKED = "TRUE";
    private static final long ONE_MILLI_NANOS = 1000000L;
    // 默认超时时间（毫秒）
    private static final long DEFAULT_TIME_OUT = 3000;
    private static final Random r = new Random();
    // 锁的超时时间（秒），过期删除
    private static final int EXPIRE = 5 * 60;
    // 锁状态标志
    private boolean locked = false;

    public boolean lock(Jedis jedis, String key) {
        return lock(jedis, key, DEFAULT_TIME_OUT);
    }

    public void unlock(Jedis jedis, String key) {
        try {
            if (locked) {
                jedis.del(key);
            }
        } finally {
            pool.returnResource(jedis);
        }
    }

    public boolean lock(Jedis jedis, String key, long timeout) {
        return lock(jedis, key, timeout, EXPIRE);
    }

    public boolean lock(Jedis jedis, String key, long timeout, int expiredTime) {
        long nanoTime = System.nanoTime();
        timeout *= ONE_MILLI_NANOS;
        try {
            while ((System.nanoTime() - nanoTime) < timeout) {
                if (jedis.setnx(key, LOCKED) == 1) {
                    logger.info("成功获得锁.key:" + key);
                    jedis.expire(key, expiredTime);
                    locked = true;
                    return true;
                }
                // 短暂休眠，nanoTime避免出现活锁
                Thread.sleep(3, r.nextInt(500));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
