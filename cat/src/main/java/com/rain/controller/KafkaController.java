package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.constant.ResultCode;
import com.rain.exception.ParamsCheckException;
import com.rain.kafkaListener.kafkaListener;
import com.rain.responseVo.ResultVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * kafka controller
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Api(description = "kafka controller")
@RestController
@RequestMapping(value = "/kafka")
@Loggable(loggable = true)
public class KafkaController extends BaseController {

    @Resource
    private KafkaTemplate<Integer, String> kafkaTemplate;

    private Logger log = LoggerFactory.getLogger(KafkaController.class);

    /**
     * 测试前需要开启zookeeper服务和kafka服务以及kafkaListener
     *
     * @see kafkaListener
     * @see com.rain.config.KafkaConfig
     **/
    @PostMapping("/testKafka")
    @Transactional
    public ResultVo testKafka(String data) {
        ListenableFuture<SendResult<Integer, String>> send = kafkaTemplate.send("hi", data);
        send.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            public void onFailure(Throwable throwable) {
                log.info("send fail!");
            }

            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                log.info("send success!");
            }
        });
        return ResultVo.success(data, "send success!");
    }

    /**
     * 测试前需要开启zookeeper服务和kafka服务以及kafkaListener
     *
     * @see kafkaListener
     * @see com.rain.config.KafkaConfig
     **/
    @PostMapping("/testKafka2")
    @Transactional
    public ResultVo testKafka2(String data) {
        kafkaTemplate.send("hello", data);
        throw new ParamsCheckException(ResultCode.PARAMETER_ERROR);
    }
}
