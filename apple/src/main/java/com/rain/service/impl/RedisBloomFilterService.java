package com.rain.service.impl;

import com.rain.constant.ResultCode;
import com.rain.exception.ServiceException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * RedisBloomFilterService
 *
 * @author lazy cat
 * 2020-07-31
 **/
@Service
public class RedisBloomFilterService {

    private static final String bloomFilterName = "isBloom";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public Boolean bloomFilter(String type, String value) {
        DefaultRedisScript<Boolean> bloom = new DefaultRedisScript<>();
        if ("add".equals(type))
            bloom.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilterAdd.lua")));
        else if ("exist".equals(type))
            bloom.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilterExist.lua")));
        else
            throw new ServiceException(ResultCode.ERROR);
        bloom.setResultType(Boolean.class);
        List<String> keyList = new ArrayList<>();
        keyList.add(bloomFilterName);
        keyList.add(value + "");
        return redisTemplate.execute(bloom, keyList);
    }
}
