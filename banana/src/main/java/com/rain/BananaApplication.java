package com.rain;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * banana启动器
 *
 * @author lazy cat
 * @since 2020-02-16
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableDubbo
public class BananaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BananaApplication.class, args);
    }
}
