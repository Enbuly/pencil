package com.rain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * spring boot启动
 *
 * @author lazy cat
 * 2020-05-28
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class FlowerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerApplication.class, args);
    }
}
