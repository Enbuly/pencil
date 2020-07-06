package com.rain.controller;

import com.rain.api.apple.model.User;
import com.rain.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private UserService userService;

    //http://localhost:8762/apple/fruit/hi?name=zzy
    @GetMapping("/hi")
    public String home(@RequestParam String name) {
        System.out.println(name + " come to client");
        return "hi " + name + " ,i am from port:" + port;
    }

    //http://localhost:8762/apple/fruit/select
    @GetMapping("/select")
    public User select() {
        User user = new User();
        user.setId("i am id");
        user.setName("zzy");
        return user;
    }

    @GetMapping(value = "/list")
    public List<User> list(User user) {
        return userService.select(user);
    }

    @PostMapping(value = "/update")
    public User update(@RequestBody User user) {
        System.out.println(user.toString() + " update success!");
        User result = new User();
        result.setId("i am id");
        result.setName("zzy");
        result.setPassword("120157");
        result.setPhone("15602227266");
        result.setSalary(20000);
        result.setStatus(0);
        return result;
    }
}