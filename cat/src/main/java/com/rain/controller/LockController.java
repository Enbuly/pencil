package com.rain.controller;

import io.swagger.annotations.Api;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * lock controller
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
                mutex.release();
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
}
