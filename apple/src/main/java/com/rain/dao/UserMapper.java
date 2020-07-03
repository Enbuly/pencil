package com.rain.dao;

import com.rain.api.apple.model.User;
import com.rain.api.apple.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 批量处理mapper
 *
 * @author lazy cat
 * @since 2019-09-14
 **/
@Mapper
public interface UserMapper {

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
    int insertUser(User user);

    /**
     * 新增用户信息
     **/
    int batchInsertUser(List<User> users);

    /**
     * 一个复杂的查询语句
     **/
    List<UserVo> selectUser();
}
