package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * thread com.rain.controller
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Api(description = "redis controller")
@RestController
@RequestMapping(value = "/redis")
@Loggable(loggable = true)
public class RedisController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    @Qualifier("redisCacheTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("测试存储string")
    @PostMapping(value = "saveString")
    public String saveString() {
        String key = StringUtils.join(new String[]{"user", "token"}, ":");
        String value = "0171826834da554b43d2c72bb1767c7898f27bf91775463e2b3b4e0f3806e0255d6e52ca286b";
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, 60, TimeUnit.SECONDS);
        return stringRedisTemplate.opsForValue().get(key);
    }

    @ApiOperation("测试存储Object-redis存string")
    @PostMapping(value = "saveObject")
    public User saveObject() {
        String value = "0171826834da554b43d2c72bb1767c7898f27bf91775463e2b3b4e0f3806e0255d6e52ca286b";
        User user = new User();
        user.setName("Tom");
        user.setPassword(value);
        String userKey = StringUtils.join(new String[]{"user", "model"}, ":");
        redisTemplate.opsForValue().set(userKey, user, 60, TimeUnit.SECONDS);
        return (User) redisTemplate.opsForValue().get(userKey);
    }

    @ApiOperation("测试存储Object-redis存hash")
    @PostMapping(value = "saveHash")
    public User saveHash() {
        User user = new User();
        user.setId("100001");
        user.setName("zzy");
        user.setPassword("123456");
        String userKey = StringUtils.join(new String[]{"user", "model"}, ":");
        redisTemplate.opsForHash().put(userKey, user.getId(), user);
        redisTemplate.expire(userKey, 60, TimeUnit.SECONDS);
        return (User) redisTemplate.opsForHash().get(userKey, user.getId());
    }

    @ApiOperation("测试存储list")
    @PostMapping("/saveList")
    public List<User> saveList() {
        String listKey = StringUtils.join(new String[]{"user", "list"}, ":");
        List<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setName("zzy");
        user1.setPassword("120157229");
        User user2 = new User();
        user2.setName("zzx");
        user2.setPassword("13828831312");
        list.add(user1);
        list.add(user2);

        redisTemplate.boundValueOps(listKey).set(list, 60, TimeUnit.SECONDS);
        return (List<User>) redisTemplate.boundValueOps(listKey).get();
    }

    @ApiOperation("测试redis的setNx命令")
    @GetMapping(value = "setNx")
    public Boolean setNx() {
        return stringRedisTemplate.opsForValue().setIfAbsent("lock", "1", 20, TimeUnit.SECONDS);
    }
}
