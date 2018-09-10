package com.netx.utils.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Closure.Yang
 * @since 2015/8/19
 */
public class KVMap {

    public Map<String, Object> fields = new HashMap<String, Object>(16);

    public static KVMap _new() {
        return new KVMap();
    }

    public static KVMap _new(String k, Object v) {
        KVMap map = new KVMap();
        map.fields.put(k, v);
        return map;
    }

    public static Map<String, Object> mapit(String k, Object v) {
        Map<String, Object> localFields = new HashMap<String, Object>(16);
        localFields.put(k, v);
        return localFields;
    }

    public KVMap kv(String k, Object v) {
        fields.put(k, v);
        return this;
    }

    public Map<String, Object> map() {
        return fields;
    }
}
