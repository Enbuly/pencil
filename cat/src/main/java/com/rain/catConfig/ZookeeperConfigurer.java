package com.rain.catConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ZookeeperConfigurer
 *
 * @author lazy cat
 * @since 2020-07-14
 **/
@Data
@Component
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperConfigurer {
    /**
     * 尝试次数
     */
    private int retryCount;

    /**
     * 重试间隔时间
     */
    private int elapsedTimeMs;

    /**
     * session超时时间
     */
    private int sessionTimeoutMs;

    /**
     * 连接超时时间
     */
    private int connectionTimeoutMs;

    /**
     * zookeeper集群地址
     */
    private String servers;

    /**
     * zookeeper分布式锁跟路径
     */
    private String lockPath;
}
