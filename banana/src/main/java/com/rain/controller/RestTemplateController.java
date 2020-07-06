package com.rain.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * restTemplate controller
 *
 * @author lazy cat
 * @since 2020-07-06
 **/
@Api(description = "restTemplate controller")
@RestController
@RequestMapping(value = "/restTemplate")
public class RestTemplateController {

    private static final String url = "http://172.20.10.2:8762/apple/fruit/hi?name=";
    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/list")
    public String list(String name) {
        return restTemplate.getForObject(url + name, String.class);
    }


}
