package com.knd.manage.config.feign;

import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

//@Configuration
public class FeignMultipartSupportConfig {

//    @Bean
    @Primary
    @Scope("prototype")
    public FormEncoder multipartFormEncoder() {
        return new SpringFormEncoder();
    }

//    @Bean
    public feign.Logger.Level multipartLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}
