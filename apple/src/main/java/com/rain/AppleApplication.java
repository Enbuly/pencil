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
        System.out.println("---------------------start apple success---------------------");
        System.out.println("swagger url is: http://localhost:8762/apple/swagger-ui.html");
        System.out.println("application port is: 8762");
        System.out.println("-------------------------------------------------------------");
    }
}