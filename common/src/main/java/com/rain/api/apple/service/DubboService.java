package com.rain.api.apple.service;

import com.rain.api.apple.model.User;

import java.util.List;

/**
 * dubbo service
 *
 * @author zhangzhenyan
 * 2020-06-23
 **/
public interface DubboService {
    List<User> query();
}
