package com.knd.permission.config;

import com.knd.common.basic.StringUtils;
import com.knd.common.userutil.UserUtils;
import com.knd.permission.bean.Token;
import com.knd.permission.util.RedisClientTokenUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Token管理器
 *
 * @author wcy
 */
@Component
@Log4j2
public class TokenManager {

    /**
     *
     */
    @Autowired
    private RedisClientTokenUtil redisClient;


    private static final String TOKEN_PREFIX = "token:";

    /**
     * web端设备标识前缀
     */
    private static final String APPKEY_PREFIX_WEB = "WEB";

    /**
     * web终端token有效期，2小时
     */
    private static final int EXPIRE_SECOND_WEB = 2 * 60 * 60;


    /**
     * 获取token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public String putToken(Token token) throws Exception {
        String tokenCode = null;
        log.info("putToken token:{{}}",token);
        if (token != null) {
            token.setExpireSecond(EXPIRE_SECOND_WEB);
            tokenCode = createTokenCode(token);
            log.info("putToken tokenCode:{{}}",tokenCode);
            String key = getKey(tokenCode);
            log.info("putToken key:{{}}",key);
            redisClient.set(key, token, token.getExpireSecond());
        }
        String key = getKey(tokenCode);
        log.info("putToken redisClient.get(getKey(tokenCode)):{{}}",redisClient.get(getKey(tokenCode)));
        log.info("putToken getKey(tokenCode):{{}}",getKey(tokenCode));
        System.out.println(redisClient.get(key));
        redisClient.set("222",token,token.getExpireSecond());
        log.info("putToken redisClient.get(key):{{}}",redisClient.get("222"));
        log.info("--------------------------------------------------------------");
        return tokenCode;
    }

    /**
     * 校验token
     *
     * @param tokenCode
     * @return
     * @throws Exception
     */
    public boolean checkToken(String tokenCode) throws Exception {
        if (!StringUtils.isEmpty(tokenCode)) {
            String key = getKey(tokenCode);
            if (redisClient.exists(key)) {
                Token token = redisClient.get(key);
                // 重置有效期
                redisClient.setExpire(key, token.getExpireSecond());
                return true;
            }
        }
        return false;
    }

    /**
     * 校验token
     *
     * @param tokenCode
     * @return
     * @throws Exception
     */
    public Token getToken(String tokenCode) throws Exception {
        Token token = null;
        if (!StringUtils.isEmpty(tokenCode)) {

            String key = getKey(tokenCode);
            token = redisClient.get(key);
        }
        return token;
    }

    /**
     * 获取token
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Token getTokenByKey(String key) throws Exception {
        Token token = null;
        if (!StringUtils.isEmpty(key)) {
            log.info("getTokenByKey key:{{}}",key);
            token = redisClient.get(key);
            log.info("getTokenByKey token:{{}}",token);
            log.info("----------------------------------------------------------");
        }
        return token;
    }

    /**
     * 获取所有tokenkey
     *
     * @param p
     * @return
     * @throws Exception
     */
    public Set<String> getTokenKeyList(String p) throws Exception {
        return redisClient.keys(p);
    }

    /**
     * 刷新token
     *
     * @param tokenCode
     * @param expireSecond
     * @return
     * @throws Exception
     */
    public String refreshToken(String tokenCode, int expireSecond) throws Exception {
        String newTokenCode = null;
        if (!StringUtils.isEmpty(tokenCode)) {
            String key = getKey(tokenCode);
            if (redisClient.exists(key)) {
                Token token = redisClient.get(key);
                redisClient.delete(key);
                newTokenCode = createTokenCode(token);
                String newKey = getKey(newTokenCode);
                if (StringUtils.isNotEmpty(expireSecond) && expireSecond > 0) {
                    token.setExpireSecond(expireSecond);
                }
                redisClient.set(newKey, token, token.getExpireSecond());
            }
        }
        return newTokenCode;
    }

    /**
     * 更新token
     *
     * @param tokenCode
     * @param token
     * @return
     * @throws Exception
     */
    public boolean updateToken(String tokenCode, Token token) throws Exception {
        if (tokenCode != null && token != null) {
            String key = getKey(tokenCode);
            if (redisClient.exists(key)) {
                token.setExpireSecond(EXPIRE_SECOND_WEB);
                redisClient.getSet(key, token, token.getExpireSecond());
                return true;
            }
        }
        return false;
    }

    /**
     * 更新token
     *
     * @param key
     * @param token
     * @return
     * @throws Exception
     */
    public boolean updateTokenByKey(String key, Token token) throws Exception {
        if (key != null && token != null) {
            if (redisClient.exists(key)) {
                token.setExpireSecond(EXPIRE_SECOND_WEB);
                redisClient.getSet(key, token, token.getExpireSecond());
                return true;
            }
        }
        return false;
    }

    /**
     * 查看token
     *
     * @param tokenCode
     * @return Token
     * @throws Exception
     */
    public Token queryToken(String tokenCode) throws Exception {
        log.info("queryToken tokenCode:{{}}",tokenCode);
        String key = getKey(tokenCode);
        log.info("queryToken key:{{}}",key);
        Token token = redisClient.get(key);
        log.info("queryToken token:{{}}",token);
        log.info("queryToken ExpireSecond:{{}}",token.getExpireSecond());
        if (token != null) {
            redisClient.setExpire(key, token.getExpireSecond());
        }
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        return null;
    }

    /**
     * 移除token
     *
     * @return Token
     * @throws Exception
     */
    public Token removeToken() throws Exception {
        Token token = null;
        String tokenCode = UserUtils.getTokenCode();
        if (!StringUtils.isEmpty(tokenCode)) {
            String key = getKey(tokenCode);
            if (redisClient.exists(key)) {
                token = redisClient.get(key);
                redisClient.delete(key);
            }
        }
        return token;
    }

    /**
     * 根据tokenCode获取key
     *
     * @param tokenCode
     * @return
     */
    private String getKey(String tokenCode) {
        return TOKEN_PREFIX + tokenCode;
    }

    /**
     * 生成tokenCode
     *
     * @param token
     * @return
     */
    private String createTokenCode(Token token) {
        String tokenCode = null;
        if (StringUtils.isNotEmptyObjects(token.getUserId(), token.getUserName())) {
            tokenCode = UserUtils.createToken(token.getUserId(), token.getPlatform());
        }

        return tokenCode;
    }

}
