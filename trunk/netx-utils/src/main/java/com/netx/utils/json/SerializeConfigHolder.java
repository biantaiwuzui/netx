package com.netx.utils.json;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.reflect.Type;

/**
 * @author Closure.Yang
 * @since 2016/3/2
 */
public class SerializeConfigHolder {
    private static final SerializeConfig serializeConfig = new SerializeConfig();

    public static void addConfig(Type key, ObjectSerializer value) {
        serializeConfig.put(key, value);
    }

    public static SerializeConfig getConfig() {
        return serializeConfig;
    }
}
