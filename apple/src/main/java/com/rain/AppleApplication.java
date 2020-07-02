package com.rain;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * apple启动
 *
 * @author lazy cat
 * 2020-05-28
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class AppleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppleApplication.class, args);
    }
}