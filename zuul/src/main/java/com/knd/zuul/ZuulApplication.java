package com.knd.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sy
 * @date : 2018/9/9 下午2:53
 * @description
 */
@EnableZuulProxy
@EnableEurekaClient
@EnableSwagger2
@SpringCloudApplication
@EnableFeignClients({"com.knd.zuul.feign"})
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

}