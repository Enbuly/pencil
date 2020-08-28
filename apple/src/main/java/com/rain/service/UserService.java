package com.rain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rain.api.apple.model.User;
import com.rain.api.apple.model.vo.UserVo;
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
     * 分页查询
     **/
    IPage<User> selectPage(int pageNumber, int pageSize);

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

    /**
     * 一个复杂的查询语句
     **/
    List<UserVo> selectUser();

    /**
     * 根据用户名查询用户密码
     *
     * @param name 用户名
     **/
    String getPassword(String name);
}
