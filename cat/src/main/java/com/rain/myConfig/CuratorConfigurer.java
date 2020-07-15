package com.rain.myConfig;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * CuratorConfigurer
 *
 * @author lazy cat
 * @since 2020-07-14
 **/
@Configuration
public class CuratorConfigurer {

    @Resource
    private ZookeeperConfigurer zkConfigurer;

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                zkConfigurer.getServers(),
                zkConfigurer.getSessionTimeoutMs(),
                zkConfigurer.getConnectionTimeoutMs(),
                new RetryNTimes(zkConfigurer.getRetryCount(), zkConfigurer.getElapsedTimeMs()));
    }

}
