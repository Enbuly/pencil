package com.rain.service.impl;

import com.rain.dao.GiraffeMapper;
import com.rain.service.GiraffeService;
import org.springframework.stereotype.Service;

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

    @Override
    public int giraffe(int id, int count) {
        return giraffeMapper.update(id, count);
    }
}
