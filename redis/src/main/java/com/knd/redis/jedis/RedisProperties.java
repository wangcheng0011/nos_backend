package com.knd.redis.jedis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class RedisProperties {
    @Value("${spring.redis.host}")
    private String hostName;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    private int maxTotal = 8;

    private int maxIdle = 8;

    private int minIdle;

    private long maxWaitMillis = -1L;

    private long minEvictableIdleTimeMillis = 1800000L;

    private long softMinEvictableIdleTimeMillis = 1800000L;

    private int numTestsPerEvictionRun = 3;

    private Boolean testOnCreate = false;

    private Boolean testOnBorrow = false;

    private Boolean testOnReturn = false;

    private Boolean testWhileIdle = false;

    private long timeBetweenEvictionRunsMillis = -1L;

    private boolean blockWhenExhausted = true;

    private int timeOut = 10000;


}
