package com.rain.dao;

import com.rain.api.apple.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * banana mapper
 *
 * @author zhangzhenyan
 * 2020-07-02
 **/
@Mapper
public interface BananaMapper {

    /**
     * 新增用户信息
     **/
    int batchInsertUser(List<User> users);

    /**
     * 查询所有的用户信息
     **/
    List<User> select(User user);
}
