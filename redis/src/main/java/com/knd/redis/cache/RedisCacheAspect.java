package com.knd.redis.cache;


import com.alibaba.fastjson.JSON;

import com.knd.redis.jedis.RedisClient;
import com.knd.common.basic.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


@Aspect
@Component
@Order(100)
public class RedisCacheAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String redisCachePrefix = "cache:";

    @Autowired
    private RedisClient jedisService;

    @Pointcut("execution(* com..service..*(..))")
    public void webAspect() {
    }

    @Around("webAspect()")
    public Object redisCache(ProceedingJoinPoint pjp) throws Throwable {
        //得到类名、方法名和参数
        String redisResult = "";
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();
        //得到被代理的方法
        Signature signature = pjp.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException();
        }
        //获取方法的参数名和参数值
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = pjp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        //得到被代理的方法上的注解
        Object annotation = method.getAnnotation(RedisCache.class);
        Object result = null;

        if (annotation != null) {
            //根据类名，方法名和参数生成key
            String key = genKey(className, methodName, args);
            logger.info("生成的key[{}]", key);
            int cacheTime = method.getAnnotation(RedisCache.class).cacheTime();
            RedisCacheEnum redisCacheEnum = method.getAnnotation(RedisCache.class).operation();
            String cacheKey = getCacheKey(methodSignature, args, method);

            if (StringUtils.isNotEmpty(cacheKey)) {
                key = cacheKey;
            }
            if (redisCacheEnum.equals(RedisCacheEnum.CREATE)) {
                if (!jedisService.exists(key)) {
                    logger.info("Redis : Cache miss");
                    //缓存不存在，则调用原方法，并将结果放入缓存中
                    result = pjp.proceed(args);
                    redisResult = JSON.toJSONString(result);
                    jedisService.set(key, redisResult, cacheTime);
                } else {
                    //缓存命中
                    logger.info("Redis : Cache hit");
                    redisResult = jedisService.get(key);
                    //得到被代理方法的返回值类型
                    Class returnType = method.getReturnType();
                    result = JSON.parseObject(redisResult, returnType);
                }
            } else if (redisCacheEnum.equals(RedisCacheEnum.UPDATE)) {
                result = pjp.proceed(args);
                redisResult = JSON.toJSONString(result);
                jedisService.set(key, redisResult, cacheTime);
            }

        } else {
            result = pjp.proceed(args);
        }

        return result;
    }

    /**
     * @Description: 生成key
     * @Param:
     * @return:
     * @Author:
     * @Date: 2018/5/16
     */
    private String genKey(String className, String methodName, Object[] args) {
        StringBuilder sb = new StringBuilder(redisCachePrefix);
        sb.append(className);
        sb.append("_");
        sb.append(methodName);
        sb.append("_");
        for (Object object : args) {
            logger.info("obj:" + object);
            if (object != null) {
                sb.append(object + "");
                sb.append("_");
            }
        }
        return sb.toString();
    }

    public String getCacheKey(MethodSignature methodSignature, Object[] args, Method method) {
        String cacheKey = method.getAnnotation(RedisCache.class).cacheKey();
        if (StringUtils.isNotEmpty(cacheKey)) {
            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext ctx = new StandardEvaluationContext();
            List<String> paramNameList = Arrays.asList(methodSignature.getParameterNames());
            List<Object> paramList = Arrays.asList(args);
            //将方法的参数名和参数值一一对应的放入上下文中
            for (int i = 0; i < paramNameList.size(); i++) {
                ctx.setVariable(paramNameList.get(i), paramList.get(i));
            }
            String cacheKeyPrefix = cacheKey.substring(0, cacheKey.indexOf("+")).replace("'", "");
            String sqElParams = cacheKey.substring(cacheKey.indexOf("+") + 2);
            String[] sqEls = sqElParams.split("#");
            for (String sqEl : sqEls) {
                //解析SpEL表达式获取值
                sqEl = sqEl.replaceAll("[^0-9a-zA-Z.()]","");
                if(StringUtils.isNotEmpty(sqEl)){
                    try {
                        String value = parser.parseExpression("#" + sqEl).getValue(ctx).toString();
                        if (StringUtils.isNotEmpty(value)) {
                            cacheKeyPrefix += value;
                        }
                    } catch (Exception e) {
                        logger.warn("param : " + sqEl + " is not exist!");
                    }
                }
            }
            return cacheKeyPrefix;
        } else {
            return null;
        }


    }


}
