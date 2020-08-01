package com.rain.service;

import org.springframework.stereotype.Service;

/**
 * FlowerService
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Service
public interface FlowerService {

    int flower(int id, int count);
}
