package com.netx.common.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisInfoHolder {
    @Value("${redisbase.host}")
    private String host;

    @Value("${redisbase.password}")
    private String password;

    @Value("${redisbase.port}")
    private Integer port;

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPort() {
        return port;
    }
}
