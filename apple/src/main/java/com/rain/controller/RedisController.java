package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis controller
 *
 * 笔记:
 * redis的key都是string类型的
 * 而value有string、list、hash、set、sorted set
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

    @ApiOperation("opsForHash存储hash")
    @PostMapping(value = "opsForHash")
    public User saveHash() {
        User user = new User();
        user.setId("100001");
        user.setName("zzy");
        user.setPassword("123456");
        String userKey = StringUtils.join(new String[]{"user", "hash"}, ":");
        redisTemplate.opsForHash().put(userKey, user.getId(), user);
        redisTemplate.expire(userKey, 60, TimeUnit.SECONDS);
        return (User) redisTemplate.opsForHash().get(userKey, user.getId());
    }

    @ApiOperation("boundValueOps存储list")
    @PostMapping("/boundValueOps")
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
        List<User> users = (List<User>) redisTemplate.boundValueOps(listKey).get();
        if (!CollectionUtils.isEmpty(users))
            for (User user : users)
                System.out.println(user.toString());
        return users;
    }

    @ApiOperation("opsForList存储list")
    @PostMapping("/opsForList")
    public List<User> saveList2() {
        String listKey = StringUtils.join(new String[]{"user", "list2"}, ":");
        User user1 = new User();
        user1.setId("100001");
        user1.setName("zzz");
        user1.setPassword("120157");
        user1.setPhone("15602227266");
        User user2 = new User();
        user2.setId("100002");
        user2.setName("xxx");
        user2.setPassword("120157");
        user2.setPhone("13828831312");

        redisTemplate.opsForList().rightPush(listKey, user1);
        redisTemplate.opsForList().rightPush(listKey, user2);
        redisTemplate.expire(listKey, 60, TimeUnit.SECONDS);
        List<User> users = (List<User>) (List) redisTemplate.opsForList().range(listKey, 0, -1);
        if (!CollectionUtils.isEmpty(users))
            for (User user : users)
                System.out.println(user.toString());
        return users;
    }

    @ApiOperation("测试redis的setNx命令")
    @GetMapping(value = "setNx")
    public Boolean setNx() {
        return stringRedisTemplate.opsForValue().setIfAbsent("lock", "1", 20, TimeUnit.SECONDS);
    }
}
