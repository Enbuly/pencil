package com.rain.controller;

import com.rain.annotation.limit.RateLimit;
import com.rain.responseVo.ResultVo;
import com.rain.service.ThreadServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * hello服务
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@RestController
@RequestMapping(value = "/guavaLimit")
public class GuavaLimitController {

    @Resource
    private ThreadServer threadServer;

    //http://localhost:8762/apple/fruit/testLimit
    @GetMapping("/testLimit")
    @RateLimit(limitKey = "testLimit", limitCount = "10")
    public ResultVo<String> testLimit() throws Exception {
        threadServer.doTaskFifth();
        return ResultVo.success("异步任务执行完毕...");
    }
}
