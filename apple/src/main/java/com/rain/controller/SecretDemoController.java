package com.rain.controller;


import com.rain.annotation.secret.SecretBody;
import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * secret example
 *
 * @author lazy cat
 * @since 2020-01-07
 **/
@Api(description = "secret example")
@RestController
@RequestMapping(value = "/secret")
public class SecretDemoController {

    private final Logger log = LoggerFactory.getLogger(SecretDemoController.class);

    /**
     * 测试http报文解密
     **/
    @SecretBody
    @PostMapping("secretBody")
    public void secretBody(@RequestBody User user) {
        log.info(user.toString());
    }
}
