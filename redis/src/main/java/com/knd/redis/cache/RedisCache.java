package com.knd.redis.cache;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface RedisCache {

    String cacheKey() default "";

    /**
     * @Description: 数据缓存时间单位s秒
     * @Param: 默认120分钟
     * @return:
     * @Author:
     * @Date: 2018/5/16
     */
    int cacheTime() default 60*60*2;

    RedisCacheEnum operation() default RedisCacheEnum.CREATE;

}
