package com.rain.controller;

import com.rain.api.apple.model.User;
import com.rain.responseVo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Api(description = "redisson Controller")
@RestController
@RequestMapping(value = "/redLock")
@Slf4j
public class RedissonController extends BaseController {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    @Qualifier("redisCacheTemplate")
    private RedisTemplate<String, Object> redisCacheTemplate;

    /**
     * redis集群模式下的分布式锁的实现
     **/
    @GetMapping(value = "/testRedLock")
    public ResultVo<Boolean> testRedLock() throws Exception {
        RLock lock = redissonClient.getLock("anyLock");
        lock.lock(20, TimeUnit.SECONDS);
        Thread.sleep(10000);
        log.info("lock it success!");
        lock.unlock();
        return ResultVo.success("lock success!>>>unlock success!");
    }

    /**
     * redis集群模式下的分布式锁的实现
     **/
    @GetMapping(value = "/testRedLock2")
    public void testRedLock2() {
        RLock lock = redissonClient.getLock("anyLock");
        lock.lock(20, TimeUnit.SECONDS);
        log.info("lock it success!");
        lock.unlock();
    }

    @ApiOperation("测试存储string")
    @PostMapping(value = "saveString")
    public String saveString() {
        String key = StringUtils.join(new String[]{"user", "token"}, ":");
        String value = "123";
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, 60, TimeUnit.SECONDS);
        return stringRedisTemplate.opsForValue().get(key);
    }

    @ApiOperation("测试存储Object")
    @PostMapping(value = "saveObject")
    public User saveObject() {
        String value = "13828831312";
        User user = new User();
        user.setName("lazyCat");
        user.setPhone(value);
        String userKey = StringUtils.join(new String[]{"user", "model"}, ":");
        redisCacheTemplate.opsForValue().set(userKey, user, 30, TimeUnit.SECONDS);
        return (User) redisCacheTemplate.opsForValue().get(userKey);
    }
}
