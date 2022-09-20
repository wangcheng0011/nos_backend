package com.knd.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableTransactionManagement
@SpringCloudApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.knd.*"})
@MapperScan("com.knd.**.mapper")
//开启feign扫描指定包
@EnableFeignClients(basePackages = {"com.knd.manage.**.service.feignInterface"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class ManageApplication {


    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }
}
