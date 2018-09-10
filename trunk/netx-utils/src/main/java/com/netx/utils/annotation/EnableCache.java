package com.netx.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Inherited
@java.lang.annotation.Documented
@Target(ElementType.TYPE)
public @interface EnableCache {
    int expire() default -1; // 过期时间，单位：秒

    int randomMin() default -1; // 是否将过期时间 再 乘以 一个随机数， 随机数区间最小值
    int randomMax() default -1; // 是否将过期时间 再 乘以 一个随机数， 随机数区间最大值

    Method[] excludeMethods() default {};

    enum Method {
        COUNT,
        LIST,
        GETBY,
        GET,
    }
}

