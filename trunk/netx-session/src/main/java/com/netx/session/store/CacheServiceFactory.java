package com.netx.session.store;

import com.netx.session.config.SessionConfig;
import com.netx.session.config.SessionConfigFactory;

import java.lang.reflect.Constructor;


public class CacheServiceFactory {

    private static CacheService cacheService;

    static {
        SessionConfig sessionConfig = SessionConfigFactory.getSessionConfig();
        if (null == sessionConfig) {
            throw new RuntimeException("初始化cacheService出错，原因是session框架未初始化");
        }

        // cacheService = new RedisCacheServiceImpl(sessionConfig.getGlobalConfigInfo("cacheAddress"));

        try {
            Class clazz = Class.forName(sessionConfig.getGlobalConfigInfo("cacheImplClass"));
            Constructor constructor = clazz.getConstructor(String.class);
            cacheService = (CacheService) constructor.newInstance(sessionConfig.getGlobalConfigInfo("cacheAddress"));
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static CacheService getCacheService() {
        return cacheService;
    }

}