package com.rain.study.AboutMessageTable;

import com.rain.constant.ResultCode;
import com.rain.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式事务-本地消息表
 *
 * @author lazy cat
 * 2020-08-07
 **/
public class MessageTable {

    @Resource
    private KafkaTemplate<Integer, String> kafkaTemplate;

    private ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();

    private Logger log = LoggerFactory.getLogger(MessageTable.class);

    public static void main(String[] args) {
        new MessageTable().method();
    }

    //分布式事务-本地消息表-服务y
    @Transactional
    public void save() {
        //1、新增数据到表A 2、新增数据到表C
        //3、set c.state=1 and c.id=雪花算法id
        //4、说明:table a and table c in db1, table b and table d in db2
    }

    public void select() {
        //select * from c where c.state=1
        //foreach c ->
        ListenableFuture<SendResult<Integer, String>> send = kafkaTemplate.send("hi", "c.id");

        send.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            public void onFailure(Throwable throwable) {
                int result = concurrentHashMap.get("c.id");
                if (result > 5) {
                    //update c.state=3
                    log.info("发送短信通知人工处理");
                    throw new ServiceException(ResultCode.ERROR);
                } else {
                    result = result + 1;
                    concurrentHashMap.put("c.id", result);
                    select();
                    //break
                }
            }

            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                //update c.state=2
                log.info("消息成功转发至mq!");
            }
        });
    }

    private void method() {
        save();
        select();
    }
}
