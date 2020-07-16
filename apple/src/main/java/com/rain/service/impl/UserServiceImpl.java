package com.rain.service.impl;

import com.rain.api.apple.model.User;
import com.rain.api.apple.model.vo.UserVo;
import com.rain.constant.ResultCode;
import com.rain.dao.UserMapper;
import com.rain.exception.ServiceException;
import com.rain.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.rain.util.Snowflake.getSnowflakeId;

/**
 * UserServiceImpl
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> select(User user) {
        return userMapper.select(user);
    }

    @Override
    public int insertUser(User user) throws Exception {
        user.setId(getSnowflakeId());
        return userMapper.insertUser(user);
    }

    @Override
    public int batchInsertUser(List<User> users) throws Exception {
        for (User user : users) {
            user.setId(getSnowflakeId());
        }
        return userMapper.batchInsertUser(users);
    }

    @Override
    public int updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

    @Override
    public List<UserVo> selectUser() {
        return userMapper.selectUser();
    }

    @Override
    public String getPassword(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new ServiceException(ResultCode.PARAMETER_ERROR);
        }
        return userMapper.selectPasswordByUserName(name);
    }
}
