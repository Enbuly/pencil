package com.rain.service;

import com.rain.dao.FlowerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * FlowerServiceImpl
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Service
public class FlowerServiceImpl implements FlowerService {

    @Resource
    private FlowerMapper flowerMapper;

    @Override
    @Transactional
    public int flower(int id, int count) {
        if (count == 500)
            throw new RuntimeException("异常丫");
        return flowerMapper.update(id, count);
    }
}
