package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.annotation.limit.RateLimit;
import com.rain.responseVo.ResultVo;
import com.rain.service.ThreadServer;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * thread com.rain.controller
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Api(description = "async controller")
@RestController
@RequestMapping(value = "/async")
@Loggable(loggable = true)
public class AsyncController extends BaseController {

    @Resource
    private ThreadServer threadServer;

    @PostMapping("/testAsyncTask")
    public ResultVo testAsyncTask() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        threadServer.doTaskOne(countDownLatch);
        threadServer.doTaskTwo(countDownLatch);
        threadServer.doTaskThree(countDownLatch);
        countDownLatch.await();
        return ResultVo.success("异步任务执行完毕...");
    }

    @PostMapping("/testFuture")
    public ResultVo testFuture() throws Exception {
        Future<String> future = threadServer.doTaskFourth();
        return ResultVo.success(future.get());
    }
}
