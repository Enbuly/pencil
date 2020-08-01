package com.rain.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务service
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@FeignClient(value = "service-flower", fallback = ServiceHiHystrix.class)
public interface ServiceHi {

    @RequestMapping(value = "/flower/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
