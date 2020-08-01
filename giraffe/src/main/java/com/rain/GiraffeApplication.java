package com.rain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * spring boot启动
 *
 * @author lazy cat
 * 2020-05-28
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GiraffeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiraffeApplication.class, args);
    }
}
