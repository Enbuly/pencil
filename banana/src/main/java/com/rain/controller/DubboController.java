package com.rain.controller;

import com.rain.api.apple.model.User;
import com.rain.api.apple.service.DubboService;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务controller
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@Api(description = "dubbo远程调用demo")
@RestController
@RequestMapping(value = "/fruit")
public class DubboController {

    @Reference(retries = 3, timeout = 4000, loadbalance = "random")
    private DubboService dubboService;

    //http://localhost:8763/banana/fruit/hello?name=zzy&email=120157229
    @GetMapping(value = "/hello")
    public String sayHello(@RequestParam String name, @RequestParam String email) {
        System.out.println("come to hello method!");
        return "welcome to banana! " + name + ", you email is " + email;
    }

    //http://localhost:8763/banana/fruit/dubboDemo
    @GetMapping(value = "dubboDemo")
    public List<User> dubboDemo() {
        return dubboService.query();
    }
}
