package com.rain.service.impl;

import com.rain.api.apple.model.User;
import com.rain.api.apple.service.DubboService;
import com.rain.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * dubbo serviceImpl
 *
 * @author zhangzhenyan
 * 2020-06-23
 **/
@Service
@Component
public class DubboServiceImpl implements DubboService {

    @Resource
    private UserService userService;

    @Override
    public List<User> query() {
        return userService.select(new User());
    }
}
