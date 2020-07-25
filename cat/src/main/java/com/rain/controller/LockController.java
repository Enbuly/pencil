package com.rain.controller;

import com.rain.catConfig.CuratorConfigurer;
import com.rain.catConfig.ZookeeperConfigurer;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper实现分布式锁-curator(zookeeper客户端框架)
 *
 * 涉及的两个class
 * @see CuratorConfigurer
 * @see ZookeeperConfigurer
 *
 * @author lazy cat
 * @since 2020-07-14
 **/
@Api(description = "lock controller")
@RestController
@RequestMapping(value = "/lock")
public class LockController {

    @Resource
    private CuratorFramework curatorFramework;

    @Resource
    private ZookeeperConfigurer zookeeperConfigurer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "lock")
    public String lock(String name) {
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, zookeeperConfigurer.getLockPath());
        try {
            if (mutex.acquire(0, TimeUnit.SECONDS)) {
                Thread.sleep(10000);
                return "hi " + name;
            }
            return "hello " + name;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "lock2")
    public String lock2(String name) {
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, zookeeperConfigurer.getLockPath());
        try {
            if (mutex.acquire(0, TimeUnit.SECONDS)) {
                return "hi " + name;
            }
            return "hello " + name;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //分布式锁解决超卖问题
    //redis减库存完成后用消息队列减数据库库存
    @PostMapping(value = "countDown")
    public String countDown() {
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, zookeeperConfigurer.getLockPath());
        try {
            if (mutex.acquire(0, TimeUnit.SECONDS)) {
                String count = stringRedisTemplate.opsForValue().get("count");

                if (!StringUtils.isEmpty(count) && Integer.parseInt(count) > 0) {
                    stringRedisTemplate.opsForValue().decrement("count");
                }
                return stringRedisTemplate.opsForValue().get("count");
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
