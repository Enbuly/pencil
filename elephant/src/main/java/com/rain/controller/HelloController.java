package com.rain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author lazy cat
 * 2020-07-30
 **/
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    //localhost:8767/elephant/hello/hello?name=zzy
    //打成war包的访问地址：
    //http://localhost:8080/elephant/hello/hello?name=zzy
    @GetMapping(value = "hello")
    public String hello(String name) {
        return "welcome to elephant!" + name;
    }
}
