package com.rain.service;

import org.springframework.stereotype.Component;

/**
 * 熔断器
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@Component
public class ServiceHiHystrix implements ServiceHi {

    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry " + name;
    }
}
