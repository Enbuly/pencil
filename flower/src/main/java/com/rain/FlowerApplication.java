package com.rain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * spring boot启动
 *
 * @see com.rain.exception.handler.GlobalExceptionHandler 注意:全局异常处理会导致openfeign
 * 调用异常不传递，导致分布式事务失败。
 * @author lazy cat
 * 2020-05-28
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class FlowerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerApplication.class, args);
    }
}
