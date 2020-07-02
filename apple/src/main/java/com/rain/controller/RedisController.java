package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private RedisTemplate<String, Object> redisCacheTemplate;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 演示redis的使用
     **/
    @PostMapping("/testRedis")
    public void testRedis() {
        //测试存储string
        String key = StringUtils.join(new String[]{"user", "token"}, ":");
        String value = "0171826834da554b43d2c72bb1767c7898f27bf91775463e2b3b4e0f3806e0255d6e52ca286b";
        stringRedisTemplate.opsForValue().set(key, value);
        System.out.println(stringRedisTemplate.opsForValue().get(key));
        stringRedisTemplate.expire(key, 60, TimeUnit.SECONDS);

        //测试存储Object
        User user = new User();
        user.setName("Tom");
        user.setPassword(value);
        String userKey = StringUtils.join(new String[]{"user", "model"}, ":");
        redisCacheTemplate.opsForValue().set(userKey, user);
        User demo = (User) redisCacheTemplate.opsForValue().get(userKey);
        System.out.println(demo);
        stringRedisTemplate.expire(userKey, 60, TimeUnit.SECONDS);

        //测试存储list
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

        redisCacheTemplate.boundValueOps(listKey).set(list);
        List<User> userList = (List<User>) redisCacheTemplate.boundValueOps(listKey).get();
        if (null != userList && userList.size() > 0) {
            for (User n : userList) {
                System.out.println(n);
            }
        }
        stringRedisTemplate.expire(listKey, 60, TimeUnit.SECONDS);
    }

    /**
     * 演示redis的使用
     **/
    @PostMapping("/testRedisTwo")
    public void testRedisTwo() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> strings = new ArrayList<>();
        strings.add("鬼泣5");
        strings.add("荒野大镖客2");
        strings.add("仙剑奇侠传7");
        listOperations.leftPushAll("list", strings);
        listOperations.leftPush("list", "hello");
        listOperations.rightPush("list", "redis");
        Long size = redisTemplate.opsForList().size("list");
        // 从list取数据，list 是 key， 第二个参数是开始取的下标，从0开始，第三个参数是结束的下标
        List<String> list = null;
        if (size != null)
            list = listOperations.range("list", 0, size);
        if (list != null)
            System.out.println(list.toString());
    }
}
