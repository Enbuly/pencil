package com.rain.kafkaListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener
 *
 * @author lazy cat
 * @since 2019-07-4
 **/
@Component
public class kafkaListener2 {

    private Logger log = LoggerFactory.getLogger(kafkaListener2.class);

    @KafkaListener(topics = "hi", groupId = "group-first")
    public void listen(String message) {
        log.info("KafkaListener2 -> " + message);
    }
}