package com.rain.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * remote controller
 *
 * @author lazy cat
 * 2020-07-06
 **/
@RestController
@RequestMapping(value = "/fruit")
public class RemoteController {

    @Value("${server.port}")
    String port;

    //http://localhost:8762/apple/fruit/hi?name=zzy
    @GetMapping("/hi")
    public String home(@RequestParam String name) {
        System.out.println(name + " come to client");
        return "hi " + name + " ,i am from port:" + port;
    }
}
