package com.netx.utils.cache;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface NetxRedisCache {
    int expire() default 300; //second
}
