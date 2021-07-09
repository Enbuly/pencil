package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.constant.ResultCode;
import com.rain.exception.ParamsCheckException;
import com.rain.responseVo.ResultVo;
import com.rain.service.UserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Login Controller
 *
 * @author lazy cat
 * @since 2019-04-25
 **/
@Api(description = "login controller")
@RestController
@RequestMapping(value = "/userManagement")
@Loggable(loggable = true)
public class LoginController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @GetMapping(value = "/login")
    public ResultVo<String> login(@RequestParam(value = "user_name") String name,
                                  @RequestParam(value = "password") String password) {

        log.info("name: " + name + ", password: " + password + "!");

        checkName(name);
        checkPassword(password);

        String token;
        if (password.equals(userService.getPassword(name))) {
            token = new Date().getTime() + name;
            stringRedisTemplate.opsForValue().setIfAbsent(token, name, 2, TimeUnit.MINUTES);
            log.info("redis set :" + token);
        } else {
            throw new ParamsCheckException(ResultCode.USER_PASSWORD_ERROR);
        }

        return ResultVo.success(token, "login success!");
    }

    @GetMapping(value = "/checkLoginOrNot")
    public ResultVo<String> checkLoginOrNot(@RequestHeader(value = "token") String token) {

        checkToken(token);

        String name = stringRedisTemplate.opsForValue().get(token);

        if (!StringUtils.isEmpty(name)) {
            return ResultVo.success("true", "该用户已经登陆!");
        } else {
            return ResultVo.success("false", "该用户未登陆!");
        }
    }
}