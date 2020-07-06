package com.rain.controller;

import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

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

    @Resource
    private RestTemplate restTemplate;

    @Value("${banana.appleHi}")
    private String url;

    @GetMapping(value = "/getString")
    public String getString(String name) {
        return restTemplate.getForObject(url + name, String.class);
    }

    @GetMapping(value = "/getObject")
    public User getObject() {
        final String GET_OBJECT = "http://127.0.0.1:8762/apple/fruit/select";
        return restTemplate.getForObject(GET_OBJECT, User.class);
    }

    @GetMapping(value = "/getList")
    public List<User> getList() {
        final String GET_LIST = "http://127.0.0.1:8762/apple/fruit/list";
        User[] users = restTemplate.getForObject(GET_LIST, User[].class);
        if (users != null)
            return Arrays.asList(users);
        return null;
    }

    @GetMapping(value = "/getAndHaveArgs")
    public List<User> getAndHaveArgs() {
        final String GET_AND_HAVE_ARGS = "http://127.0.0.1:8762/apple/fruit/list?name=zzy";
        User[] users = restTemplate.getForObject(GET_AND_HAVE_ARGS, User[].class);
        if (users != null)
            return Arrays.asList(users);
        return null;
    }
}