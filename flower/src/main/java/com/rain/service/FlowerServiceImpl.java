package com.rain.service;

import com.rain.dao.FlowerMapper;
import org.springframework.stereotype.Service;

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
    public int flower(int id, int count) {
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flowerMapper.update(id, count);
    }
}
