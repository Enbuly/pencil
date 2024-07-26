package com.rain.service.impl;

import com.rain.service.ThreadServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * 异步服务实现
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Service
public class ThreadServerImpl implements ThreadServer {

    private final static Random random = new Random();

    private final Logger logger = LoggerFactory.getLogger(ThreadServerImpl.class);

    @Async("asyncServiceExecutor")
    public void doTaskOne(CountDownLatch countDownLatch) throws Exception {
        logger.info("开始做任务一...");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        logger.info("完成任务一，耗时：" + (end - start) + "毫秒");
        countDownLatch.countDown();
    }

    @Async("asyncServiceExecutor")
    public void doTaskTwo(CountDownLatch countDownLatch) throws Exception {
        logger.info("开始做任务二...");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        logger.info("完成任务二，耗时：" + (end - start) + "毫秒");
        countDownLatch.countDown();
    }

    @Async("asyncServiceExecutor")
    public void doTaskThree(CountDownLatch countDownLatch) throws Exception {
        logger.info("开始做任务三...");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        logger.info("完成任务三，耗时：" + (end - start) + "毫秒");
        countDownLatch.countDown();
    }

    @Async("asyncServiceExecutor")
    public Future<String> doTaskFourth() throws Exception {
        logger.info("开始做任务四...");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        long result = end - start;
        logger.info("完成任务四，耗时：" + result + "毫秒");
        return new AsyncResult<>("任务4耗时" + result);
    }

    @Async("asyncServiceExecutor")
    public void doTaskFifth() throws Exception {
        logger.info("开始做任务五...");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        logger.info("完成任务五，耗时：" + (end - start) + "毫秒");
    }
}
