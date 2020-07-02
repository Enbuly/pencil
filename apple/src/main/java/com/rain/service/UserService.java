package com.rain.service;

import com.rain.api.apple.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Service
public interface UserService {

    /**
     * 查询所有的用户信息
     **/
    List<User> select(User user);

    /**
     * 根据用户id修改用户信息
     **/
    int updateUserById(User user);

    /**
     * 新增用户信息
     **/
    int insertUser(User user) throws Exception;

    /**
     * 新增用户信息
     **/
    int batchInsertUser(List<User> users) throws Exception;
}
