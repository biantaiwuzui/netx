package com.netx.utils.cache;

import com.netx.utils.datastructures.Tuple;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

class ConcurrentDoubleStringKeyHashMap {
    private ConcurrentHashMap<String, BaseValue> map = null;

    public ConcurrentDoubleStringKeyHashMap() {
        map = new ConcurrentHashMap<>();
    }

    public ConcurrentDoubleStringKeyHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    private MarkConcurrentHashMap<BaseValue> createK2Map() {
        return new MarkConcurrentHashMap<>(8, 1.0f);
    }

    private void checkK(String... keys) {
        for(String key : keys) {
            if(key == null) throw new RuntimeException("key不可以为null");
        }
    }

    private boolean isNotNullMarkConcurrentHashMap(BaseValue o) {
        return (o != null && o.getValue() != null && o.getValue() instanceof MarkConcurrentHashMap);
    }

    public void put(String key, BaseValue value) {
        checkK(key);
        if(!value.isExpired()) {
            map.put(key, value);
        }
    }

    public void put(String key1, String key2, BaseValue value) {
        checkK(key1, key2);
        if(!value.isExpired()) {
            BaseValue o = map.get(key1);
            MarkConcurrentHashMap mapLevel2;
            if(isNotNullMarkConcurrentHashMap(o)) {
                mapLevel2 = (MarkConcurrentHashMap)o.getValue();
                mapLevel2.put(key2, value);
            } else {
                map.compute(key1, (k, v) -> {
                        if(isNotNullMarkConcurrentHashMap(v)) {
                            ((MarkConcurrentHashMap)o.getValue()).put(key2, value);
                            return v;
                        } else {
                            MarkConcurrentHashMap<BaseValue> mp = createK2Map();
                            mp.put(key2, value);
                            BaseValue bv = new BaseValue(mp);
                            return bv;
                        }
                    });
            }
        }
    }

    public BaseValue get(String key) {
        checkK(key);
        BaseValue bv = map.get(key);
        if(bv != null && bv.isExpired()) {
            bv = map.computeIfPresent(key, (k, v) -> {
                                  if(v != null && v.isExpired()) {
                                      return null;
                                  } else {
                                      return v;
                                  }
                              });
        }
        return bv;
    }

    public BaseValue get(String key1, String key2) {
        checkK(key1, key2);
        BaseValue o = map.get(key1);
        if(!isNotNullMarkConcurrentHashMap(o)) return null;
        MarkConcurrentHashMap<BaseValue> mapLevel2 = (MarkConcurrentHashMap)o.getValue();
        BaseValue bv = mapLevel2.get(key2);
        if(bv != null && bv.isExpired()) {
            bv = mapLevel2.computeIfPresent(key2, (k, v) -> {
                                        if(v != null && v.isExpired()) {
                                            return null;
                                        } else {
                                            return v;
                                        }
                                    });
        }
        return bv;
    }

    /**
    Tuple.left标识了返回的是否是Default值，true是Supplier产生的Default值，false则表示value从map中取出来的。
    <br/>Supplier产生的Default值不会存入map中
    @param key
    @param supplier 延迟给予的Default产生器。
    @return 
     */
    public Tuple<Boolean, BaseValue> getOrDefault(String key, Supplier<BaseValue> supplier) {
        BaseValue value = get(key);
        if(value == null) {
            return new Tuple<>(true, supplier.get());
        } else {
            return new Tuple<>(false, value);
        }
    }

    /**
    Tuple.left标识了返回的是否是Default值，true是Supplier产生的Default值，false是从map中取出来的
    @param supplier 延迟给予的Default产生器。
    @return 
     */
    public Tuple<Boolean, BaseValue> getOrDefault(String key1, String key2, Supplier<BaseValue> supplier) {
        BaseValue value = get(key1, key2);
        if(value == null) {
            return new Tuple<>(true, supplier.get());
        } else {
            return new Tuple<>(false, value);
        }
    }

    public boolean exists(String key) {
        BaseValue bv = get(key);
        return bv != null;
    }

    public boolean exists(String key1, String key2) {
        BaseValue bv = get(key1, key2);
        return bv != null;
    }

    /**删除一个映射并返回旧值*/
    public Object delete(String key) {
        return map.remove(key);
    }

    /**删除一个映射并返回旧值*/
    public Object delete(String key1, String key2) {
        BaseValue value = get(key1);
        if(isNotNullMarkConcurrentHashMap(value)) {
            return ((MarkConcurrentHashMap)value.getValue()).remove(key2);
        }
        return null;
    }

    private boolean isNeedType(Class<?> clazz) {
        return (clazz == int.class || clazz == Integer.class
                || clazz == long.class || clazz == Long.class
                || clazz == short.class || clazz == Short.class
                || clazz == byte.class || clazz == Byte.class);
    }

    /**
    原子给一个key的值加increment，并返回增加后的值。
    如果这个key不存在，会将increment作为key的value存储起来，并返回increment。
    如果key映射的原值不是byte、short、int、long及其包装类型，会用increment覆盖并返回increment。
    @param increment
    @return 
     */
    public BaseValue<Long> incrBy(String key, BaseValue<Long> value) {
        BaseValue compute = map.compute(key, (k, v) -> {
                                    if(v == null || v.isExpired() || v.getValue() == null || !isNeedType(v.getValue().getClass())) {
                                        return value;
                                    } else {
                                        long l = Long.parseLong(v.getValue().toString()) + value.getValue();
                                        return new BaseValue(l, value.getExpireTime());
                                    }
                                });
        return compute;
    }

    /**
    原子给一个双key的值加increment，并返回增加后的值。
    如果这个key不存在，会将increment作为key的value存储起来，并返回increment。
    如果key映射的原值不是byte、short、int、long及其包装类型，会用increment覆盖并返回increment。
    @param increment
    @return 
     */
    public BaseValue<Long> incrBy(String key1, String key2, BaseValue<Long> value) {
        checkK(key1, key2);

        BaseValue<MarkConcurrentHashMap<BaseValue<Long>>> compute
                                                          = map.compute(key1, (k, v) -> {
                                                                    if(isNotNullMarkConcurrentHashMap(v)) {
                                                                        MarkConcurrentHashMap<BaseValue> mapLevel2 = (MarkConcurrentHashMap)v.getValue();
                                                                        mapLevel2.compute(key2, (k2, v2) -> {
                                                                                      if(v2 == null || v2.isExpired() || v2.getValue() == null || !isNeedType(v2.getValue().getClass())) {
                                                                                          return value;
                                                                                      } else {
                                                                                          long l = Long.parseLong(v2.getValue().toString()) + value.getValue();
                                                                                          return new BaseValue(l, value.getExpireTime());
                                                                                      }
                                                                                  });
                                                                        return v;
                                                                    } else {
                                                                        MarkConcurrentHashMap<BaseValue> mp = createK2Map();
                                                                        mp.put(key2, value);
                                                                        BaseValue bv = new BaseValue(mp);
                                                                        return bv;
                                                                    }
                                                                });

        return compute.getValue().get(key2);
    }

    void expiredResourceCollect() {
        for(Map.Entry<String, BaseValue> entry : map.entrySet()) {
            String key = entry.getKey();
            BaseValue value = entry.getValue();

            if(isNotNullMarkConcurrentHashMap(value)) {
                MarkConcurrentHashMap<BaseValue> mapLevel2 = (MarkConcurrentHashMap)value.getValue();
                for(Map.Entry<String, BaseValue> entry2 : mapLevel2.entrySet()) {
                    String key2 = entry2.getKey();
                    BaseValue value2 = entry2.getValue();

                    if(value2 != null && value2.isExpired()) {
                        mapLevel2.computeIfPresent(key2, (k, v) -> {
                                               if(v != null && v.isExpired()) {
                                                   return null;
                                               } else {
                                                   return v;
                                               }
                                           });
                    }
                }
                if(mapLevel2.isEmpty()) {
                    map.computeIfPresent(key, (k, v) -> {
                                     if(isNotNullMarkConcurrentHashMap(v) && (v.isExpired() || ((MarkConcurrentHashMap)v.getValue()).isEmpty())) {
                                         return null;
                                     } else {
                                         return v;
                                     }
                                 });
                }
            } else if(value != null && value.isExpired()) {
                map.computeIfPresent(key, (k, v) -> {
                                 if(v != null && v.isExpired()) {
                                     return null;
                                 } else {
                                     return v;
                                 }
                             });
            }
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
