package com.rain.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaConfig
 *
 * @author lazy cat
 * @since 2019-07-4
 **/
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.ip}")
    private String kafkaIp;

    //kafkaTemplate实现了Kafka发送接收等功能
    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    //ConcurrentKafkaListenerContainerFactory为创建Kafka监听器的工程类，这里只配置了消费者
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        // 设置消费者工厂
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    //生产者工厂
    private ProducerFactory<Integer, String> producerFactory() {
        DefaultKafkaProducerFactory<Integer, String> producerFactory =
                new DefaultKafkaProducerFactory<>(producerConfigs());
        producerFactory.transactionCapable();
        producerFactory.setTransactionIdPrefix("tra-");
        return producerFactory;
    }

    @Bean
    public KafkaTransactionManager<Integer, String> transactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }

    //消费者工厂
    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    //生产者配置
    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        // 指定kafka集群多个地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaIp);

        // 同步到副本, 默认为1
        // acks=0 把消息发送到kafka就认为发送成功
        // acks=1 把消息发送到kafka leader分区，并且写入磁盘就认为发送成功
        // acks=all 把消息发送到kafka leader分区，并且leader分区的副本follower对消息进行了同步就任务发送成功
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        //幂等（acks=all可能存在数据重复的问题）
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        //事务id
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-");

        // 重试次数，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        // 控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        // 批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        // 生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        // 键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        // 值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    //消费者配置
    private Map<String, Object> consumerConfigs() {
        HashMap<String, Object> props = new HashMap<>();

        // Kafka地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaIp);

        // 配置分组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "hello");

        // 是否自动提交offset偏移量(默认true)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        // 自动提交的频率(ms)
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        // 键的反序列化方式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

        // 值的反序列化方式
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

}