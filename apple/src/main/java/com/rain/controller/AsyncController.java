package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.responseVo.ResultVo;
import com.rain.service.ThreadServer;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * spring的异步任务
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Api("spring的异步任务")
@RestController
@RequestMapping(value = "/async")
@Loggable(loggable = true)
public class AsyncController extends BaseController {

    @Resource
    private ThreadServer threadServer;

    @PostMapping("/testAsyncTask")
    public ResultVo<String> testAsyncTask() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        // mock do task one
        threadServer.doTaskOne(countDownLatch);
        // mock do task two
        threadServer.doTaskTwo(countDownLatch);
        // mock do task three
        threadServer.doTaskThree(countDownLatch);
        countDownLatch.await();
        return ResultVo.success("post testAsyncTask end...");
    }

    @PostMapping("/testFuture")
    public ResultVo<String> testFuture() throws Exception {
        Future<String> future = threadServer.doTaskFourth();
        return ResultVo.success(future.get());
    }
}
