package com.rain.controller;

import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Api(description = "RestTemplate远程调用demo")
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
        final String GET_AND_HAVE_ARGS = "http://127.0.0.1:8762/apple/fruit/list";
        User[] users = restTemplate.getForObject(GET_AND_HAVE_ARGS + "?name=zzy", User[].class);
        if (users != null)
            return Arrays.asList(users);
        return null;
    }

    @GetMapping(value = "/getEntity")
    public List<User> getEntity() {
        final String GET_AND_HAVE_ARGS = "http://127.0.0.1:8762/apple/fruit/list";
        final int SUCCESS = 200;

        ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(GET_AND_HAVE_ARGS + "?name=zzy", User[].class);
        if (responseEntity.getStatusCodeValue() == SUCCESS && null != responseEntity.getBody())
            return Arrays.asList(responseEntity.getBody());
        return null;
    }

    @PostMapping(value = "/update")
    public User update() {
        final String UPDATE = "http://127.0.0.1:8762/apple/fruit/update";
        User user = new User();
        user.setId("i am post test");
        user.setName("you can do it");
        return restTemplate.postForObject(UPDATE, user, User.class);
    }

    @PostMapping(value = "/updates")
    public User updates() {
        final String UPDATE = "http://127.0.0.1:8762/apple/fruit/update";
        final int SUCCESS = 200;

        User user = new User();
        user.setId("i am post test");
        user.setName("you can do it");
        user.setPhone("phone");

        ResponseEntity<User> responseEntity = restTemplate.postForEntity(UPDATE, user, User.class);
        if (SUCCESS == responseEntity.getStatusCodeValue())
            return responseEntity.getBody();
        return null;
    }
}