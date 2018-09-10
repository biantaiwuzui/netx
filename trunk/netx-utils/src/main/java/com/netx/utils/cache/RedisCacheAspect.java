package com.netx.utils.cache;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class RedisCacheAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final RedisCache redisCache = new RedisCache("127.0.0.1",null,6379);

    @Around("@annotation(cache)")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint, NetxRedisCache cache) {
        try {
            String key = getRedisCacheKey(proceedingJoinPoint);
            Object value = redisCache.get(key);
            if (value != null)
                return value;
            value = proceedingJoinPoint.proceed();
            if (cache.expire() < 0) {
                redisCache.put(key, value);
            } else {
                redisCache.put(key, value, cache.expire());
            }
            return value;

        }catch (Throwable throwable){
            logger.error("redis cache pointcut failed", throwable);
            return null;
        }
    }

    private String getRedisCacheKey(ProceedingJoinPoint proceedingJoinPoint){
        StringBuilder buf = new StringBuilder();
        Signature signature = proceedingJoinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        String name = signature.getName();
        if (declaringTypeName.equals("com.netx.utils.cache.ServiceCacheImpl")) {
            declaringTypeName = proceedingJoinPoint.getTarget().getClass().getName();
        }

        String[] packages = StringUtils.split(declaringTypeName,".");
        for(String pack: packages) {
            buf.append(pack, 0, 1);
            buf.append(".");
        }
        buf.append(name);

        Object[] args = proceedingJoinPoint.getArgs();
        Annotation[][] pas = ((MethodSignature)signature).getMethod().getParameterAnnotations();
        int length = pas.length;
        for(int i = 0; i < length; ++i) {
            Annotation[] var1 = pas[i];
            int var2 = var1.length;
            for(int var3 = 0; var3 < var2; ++var3) {
                Annotation an = var1[var3];
                if(an instanceof CacheKey) {
                    String keyValue = args[i].toString();
                    if (args[i] instanceof Page) {
                        keyValue = String.valueOf( ((Page)args[i]).getCurrent());
                    }
                    buf.append("|").append(((CacheKey)an).key()).append("=").append(keyValue);
                    break;
                }
            }
        }
        return buf.toString();
    }
}
