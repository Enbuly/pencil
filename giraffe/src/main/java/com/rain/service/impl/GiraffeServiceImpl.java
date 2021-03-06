package com.rain.service.impl;

import com.rain.api.flower.Book;
import com.rain.dao.GiraffeMapper;
import com.rain.service.GiraffeService;
import com.rain.service.ServiceHi;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * GiraffeServiceImpl
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Service
public class GiraffeServiceImpl implements GiraffeService {

    @Resource
    private GiraffeMapper giraffeMapper;

    @Resource
    private ServiceHi serviceHi;

    /**
     * db action
     **/
    @Override
    public int giraffe(int id, int count) {
        return giraffeMapper.update(id, count);
    }

    /**
     * 分布式事务seata
     **/
    @Override
    @Transactional
    @GlobalTransactional(rollbackFor = Exception.class)
    public void distributedAffair() {
        giraffe(2, 600);
        System.out.println("debug in this look undo log");
        serviceHi.count(new Book(1, 500));
    }
}
