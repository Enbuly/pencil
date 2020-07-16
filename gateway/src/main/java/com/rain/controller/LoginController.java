package com.rain.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Login Controller
 *
 * @author lazy cat
 * @since 2019-04-25
 **/
@RestController
@RequestMapping(value = "/userManagement")
public class LoginController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    //http://localhost:8764/userManagement/login?user_name=zzy&password=zzy120157229
    @GetMapping(value = "/login")
    public String login(@RequestParam(value = "user_name") String name,
                        @RequestParam(value = "password") String password) throws Exception {

        log.info("name: " + name + ", password: " + password + "!");

        checkName(name);
        checkPassword(password);

        String token;
        if (password.equals("zzy120157229")) {
            token = new Date().getTime() + name;
            stringRedisTemplate.opsForValue().setIfAbsent(token, name, 5, TimeUnit.MINUTES);
            log.info("redis set :" + token);
        } else {
            throw new Exception();
        }

        return token;
    }

    private void checkName(String name) throws Exception {
        if (StringUtils.isEmpty(name)) {
            throw new Exception();
        }
    }

    private void checkPassword(String password) throws Exception {
        if (StringUtils.isEmpty(password)) {
            throw new Exception();
        }
    }

}
