## 关于zookeeper和kafka
   ### mac安装kafka
      brew install kafka
   ### kafka的启动
      1->cd到kafka的bin目录 cd /usr/local/Cellar/kafka/2.2.1/bin
      2->启动zookeeper ./zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties &
      3->启动kafka ./kafka-server-start /usr/local/etc/kafka/server.properties &
      4->查看所有篮子 kafka-topics --list --zookeeper localhost:2181
      5->创建篮子hello kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic hello
      6->向篮子hello发送消息 kafka-console-producer --broker-list 172.20.10.5:9092 --topic hello
      7->从篮子hello取出一条消息 kafka-console-consumer --bootstrap-server 172.20.10.5:9092 --topic hello --from-beginning
      8->安装配置文件的位置
      /usr/local/etc/kafka/server.properties
      /usr/local/etc/kafka/zookeeper.properties
   ### 关于kafka的应用
      1、批量操作（异步批量增加用户）
      2、异步调用其他系统的api（异步调用征信信息）
      3、频繁操作且可以异步（记录用户的位置，没隔五秒钟查看用户具体位置并记录到数据库）
   ### 记录操作日志
      1、可以直接在HttpAspect加入
      @Resource
      private KafkaTemplate<Integer, String> kafkaTemplate;
      利用kafkaTemplate发送消息
      2、在kafka监听器处接收数据并记录至数据库。
      

## kafka学习笔记
消息队列的两种模式
1、点对点模式
2、发布/订阅模式
其中，发布/订阅模式中又分消费者主动拉取数据和队列推送数据两种。

kafka是一个分布式的，基于发布/订阅模式的消息队列，主要用于大数据实时处理领域。
主要概念:
broker -> topic -> partition -> replica -> leader -> follower

创建topic:
kafka-topics --create --zookeeper localhost:2181
--replication-factor 1 --partitions 1 --topic hello
其中partition数量可以大于broker数量，replica数量不能大于broker数量
查看topic:
kafka-topics --list --zookeeper localhost:2181
kafka-topics --describe --topic hello --zookeeper localhost:2181

关于生产者

-> 分区策略
1、指明partition的情况下，直接前往这个partition写数据。
2、没有partition但有key的情况下，将key的hash值与topic
的partition数进行取余得到写入的partition的值。
3、即没有partition又没有key的情况下，第一次随机生成一个
整数(后面在这个整数上自增)，将这个值与与topic的partition
数进行取余得到写入的partition的值，也就是RoundRobin。

-> 数据可靠性ISR
leader维护了一个动态的ISR，意为和leader保持同步的follower
集合。当ISR中的follower完成数据同步之后会告知leader，
leader再给生产者发送ack。如果follower长时间未向leader
同步数据则会被踢出ISR，该时间值由replica.lag.time.max.ms
参数设定。leader发生故障之后，就会从ISR中选举新的leader。

-> ack机制
1、ack=0
把消息发送到kafka则认为发送成功。
不足：leader故障或者不故障都可能丢失数据
2、ack=1
把消息发送到kafka，leader写入磁盘并回复ack
就认为发送成功。
不足：leader故障丢失数据。
3、ack=all
把消息发送到kafka，leader写入磁盘成功，并且其他follower
也写入磁盘成功则回复ack。
不足：可能存在数据重复的问题，极限情况下(ISR里只有leader)
可能会数据丢失。

-> 数据一致性
1、HW -> high watermark
2、LEO -> log end offset
3、(消费数据一致性)HW之前的的数据对consumer可见。
4、leader发生故障之后，会从ISR中选一个新的leader，为保证
多个follower的数据一致性，其余的follower会先将各自log
文件中高于HW的部分截掉，然后从leader同步数据。
5、follower发生故障之后，会被零时踢出ISR，待该follower
恢复后，follower会读取本地磁盘记录的上次的HW值，并将log
文件中高于HW的部分截掉，从HW开始向leader同步，等该follower
的LED大于等于leader的HW，重新加入ISR。

-> 幂等
Exactly Once = 幂等 + At Least Once
1、ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG 的值为true
开启幂等性。
PID = produce id
seqNumber 序列化号
2、开启幂等性的producer会在初始化的时候分配一个PID，发往同一个
partition的消息会附带seqNumber，而broker端会对
<pid, partition, seqNumber>做缓存，当具有相同的主键的消息
提交时，broker只会持久化一次。
producer重启PID会变化，不同的partition也会有不同的主键，
所以幂等性无法保证跨会话、跨分区的Exactly Once。

-> 事务
为了实现这种效果,应用程序必须提供一个稳定的(重启后不变)唯一的
ID,也即Transaction ID。Transaction ID与PID一一对应。
区别在于Transaction ID由用户提供，将生产者的 
transactional.id配置项设置为某个唯一ID。而PID是内部的实
现对用户透明。
另外，为了保证新的Producer启动后，旧的具有相同 
Transaction ID的Producer失效，每次Producer 
通过Transaction ID拿到PID的同时，还会获取一个
单调递增的epoch。由于旧的Producer的epoch比新
Producer的epoch小，Kafka可以很容易识别出该 
Producer是老的Producer并拒绝其请求。

    使用事务配置配置：
    1、
    //结合幂等配置，保证跨会话、跨分区的Exactly Once ->还需要配合代码改动，暂未完成
    props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "100001");
     
    2、
    private ProducerFactory<Integer, String> producerFactory() {
            DefaultKafkaProducerFactory<Integer, String> producerFactory =
                    new DefaultKafkaProducerFactory<>(producerConfigs());
            producerFactory.transactionCapable();
            producerFactory.setTransactionIdPrefix("tra-");
            return producerFactory;
    }
     
    3、
    @Bean
    public KafkaTransactionManager<Integer, String> transactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }
    
    使用：
    @Transactional