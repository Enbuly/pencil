package com.rain.service;

import org.springframework.stereotype.Service;

/**
 * GiraffeService
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Service
public interface GiraffeService {

    /**
     * db action
     **/
    int giraffe(int id, int count);

    /**
     * 分布式事务seata
     **/
    void distributedAffair();
}
