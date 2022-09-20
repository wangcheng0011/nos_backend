package com.knd.zuul.config;

import com.knd.zuul.fallback.ApiFallbackProvider;
import com.knd.zuul.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hl
 * @date : 2018/9/23 下午5:10
 * @description
 */
@Configuration
public class ZuulConfig {

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public ApiFallbackProvider apiFallbackProvider() {
        return new ApiFallbackProvider();
    }

}