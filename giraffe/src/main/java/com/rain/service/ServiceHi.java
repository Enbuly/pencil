package com.rain.service;

import com.rain.api.flower.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务service
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@FeignClient(value = "service-flower")
public interface ServiceHi {

    @RequestMapping(value = "/flower/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/flower/flower", method = RequestMethod.POST)
    String count(@RequestBody Book book);
}
