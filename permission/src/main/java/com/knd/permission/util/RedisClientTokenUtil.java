/**
 *
 */
package com.knd.permission.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.knd.common.basic.DateUtils;
import com.knd.permission.bean.Token;
import com.knd.redis.jedis.RedisClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Redis操作Token封装类
 *
 * @author wcy
 *
 */
@Component
@Log4j2
public class RedisClientTokenUtil {

    /**
     * json 转换
     */
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected RedisClient redisClient;

    /**
     * 存入token
     *
     * @param key
     * @param token
     * @param timeout
     * @throws Exception
     */
    public void set(String key, Token token, int timeout) throws Exception {
        token.setLoginTime(DateUtils.getCurrentDateTimeStr());
        String value = objectMapper.writeValueAsString(token);
        redisClient.set(key, value, timeout);
    }

    /**
     * 存入token字符串
     *
     * @param key
     * @param token
     * @param timeout
     * @throws Exception
     */
    public void set(String key, String token, int timeout) throws Exception {
        redisClient.set(key, token, timeout);
    }

    /**
     * 替换存入token并设置过期时间
     *
     * @param key
     * @param token
     * @param timeout
     * @return
     * @throws Exception
     */
    public void getSet(String key, Token token, int timeout) throws Exception {
        token.setLoginTime(DateUtils.getCurrentDateTimeStr());
        String value = objectMapper.writeValueAsString(token);
        redisClient.set(key, value, timeout);
    }


    /**
     * 获取token
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Token get(String key) throws Exception {
        String value = redisClient.get(key);
        log.info("getValue value:{{}}",value);
        return value == null ? null : objectMapper.readValue(value, Token.class);
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String getStr(String key){
        return redisClient.get(key);

    }

    public Set<String> keys(String pattern) {
        return redisClient.keys(pattern + "*");
    }

    /**
     * 判断token是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisClient.exists(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @return
     */
    public Long setExpire(String key, int timeout) {
        return redisClient.expire(key, timeout);
    }

    /**
     * 删除token
     *
     * @param key
     */
    public void delete(String key) {
        redisClient.del(key);
    }

}
