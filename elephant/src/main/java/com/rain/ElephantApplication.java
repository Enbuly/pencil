package com.rain;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
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
@EnableEncryptableProperties
public class ElephantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElephantApplication.class, args);
    }
}
