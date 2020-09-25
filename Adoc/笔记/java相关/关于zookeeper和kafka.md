## 关于zookeeper和kafka的shell
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

   ### 关闭kafka和zookeeper
      cd /usr/local/Cellar/kafka/2.2.1/bin
      kafka-server-stop
      cd /usr/local/Cellar/kafka/2.2.1/bin
      zookeeper-server-stop

## kafka学习笔记

#### 消息队列-kafka
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

#### 关于kafka生产者
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
2、Kafka是如何具体实现幂等的呢？Kafka为此引入了producer id(以下简称PID)和
序列号(sequence number)这两个概念。每个新的生产者实例在初始化的时候都会被
分配一个PID，这个PID对用户而言是完全透明的。
对于每个PID，消息发送到的每一个分区都有对应的序列号，这些序列号从0开始单调递增。
生产者每发送一条消息就会将对应的序列号的值加1。
broker端会在内存中为每一对维护一个序列号。对于收到的每一条消息，只有当它的序列
号的值(SN_new)比broker端中维护的对应的序列号的值(SN_old)大1
(即SN_new = SN_old + 1)时，broker才会接收它。
如果SN_new< SN_old + 1，那么说明消息被重复写入，broker可以直接将其丢弃。
如果SN_new> SN_old + 1，那么说明中间有数据尚未写入，出现了乱序，
暗示可能有消息丢失，这个异常是一个严重的异常。
引入序列号来实现幂等也只是针对每一对而言的，也就是说，Kafka的幂等只能保证单
个生产者会话(session)中单分区的幂等。幂等性不能跨多个分区运作，而事务可以弥补这个缺陷。

-> 事务(kafka从0.11版本开始引入了事务的支持)
1、事务可以保证对多个分区写入操作的原子性。操作的原子性是指多个操作要么全部成功，
要么全部失败，不存在部分成功、部分失败的可能。
2、生产者发往多个分区可能意味着发送到多台Broker上，也就是多个进程参与到事务中。
需要采用分布式事务机制来保证原子性的写入。Kafka事务机制借鉴了两阶段提交的思想，
引入了transaction coordinator来帮助完成多个分区上的原子性写入。如果没有这
个协调组件，单靠broker自己是无法实现原子性的。
3、Kafka提供事务主要是为了实现精确一次处理语义(exactly-once semantics, EOS)的，
而EOS是实现流处理系统正确性(correctness)的基石，故Kafka事务被大量应用
于Kafka Streams之中。不过用户当然也能够调用API实现自己的事务需求。具体的场
景包括：1. producer端跨多分区的原子性写入2. consumer端读取事务消息多分区
原子性写入保证producer发送到多个分区的一批消息要么都成功要么都失败=>所谓的失
败是指对事务型consumer不可见；而consumer端读取事务消息主要由consumer端隔离
级别体现，它类似于数据库中隔离级别的概念，目前只是简单分为：read_uncommitted和
read_committed，其中后者指的是consumer只能读取已成功提交事务的消息（当然也
包括非事务型producer生产的消息）。目前Kafka事务在consumer端很难像一般的数据库
那样提供更高级的隔离级别（比如串行化或者Snapshot），即事务型consumer能保证读取
到的消息都是已提交事务的消息，但不敢保证能够读取到所有这样的消息——有很多原因会导
致这一点，比如compact topic使用新版本消息覆盖了之前的事务消息或日志段删除导致
部分数据不可读等。
4、为了使用事务，应用程序必须提供唯一的transactionalId，这个transactionalId通过
客户端参数transactional.id来显式设置。事务要求生产者开启幂等特性，因此通过
将transactional.id参数设置为非空从而开启事务特性的同时需要将enable.idempotence
设置为true（如果未显式设置，则KafkaProducer默认会将它的值设置为true），
如果用户显式地将enable.idempotence设置为false，则会报出ConfigException的异常。
transactionalId与PID一一对应，两者之间所不同的是transactionalId由
用户显式设置，而PID是由Kafka内部分配的。
5、为了保证新的生产者启动后具有相同transactionalId的旧生产者能够立即失效，
每个生产者通过transactionalId获取PID的同时，还会获取一个单调递增的producer epoch。
如果使用同一个transactionalId开启两个生产者，那么前一个开启的生产者会报错。
从生产者的角度分析，通过事务，Kafka可以保证跨生产者会话的消息幂等发送，
以及跨生产者会话的事务恢复。

    使用事务配置配置：
    1、生产者配置事务id，具体见KafkaConfig。
    消费者配置，consumer只会读取已经提交了事务的消息
    props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "100001");
    
    props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
     
    2、生产者工厂配置
    private ProducerFactory<Integer, String> producerFactory() {
            DefaultKafkaProducerFactory<Integer, String> producerFactory =
                    new DefaultKafkaProducerFactory<>(producerConfigs());
            producerFactory.transactionCapable();
            producerFactory.setTransactionIdPrefix("tra-");
            return producerFactory;
    }
     
    3、配置KafkaTransactionManager
    @Bean
    public KafkaTransactionManager<Integer, String> transactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }
    
    4、使用：
    @Transactional
    
#### 关于kafka消费者
1、多个消费者可以订阅同一个topic
2、一个消费者只能属于一个消费者组
3、消费者组订阅的topic只能被其中的一个消费者消费
4、不同消费者组的消费者可以消费同一个topic

分区分配策略(当消费者组的消费者数量发生改变，触发分区分配策略)
RoundRobin:轮询策略
Range(默认):
n = pCount / cCount   8 / 3 = 2
m = pCount % cCount  8 % 3 = 2
前m(2)个消费者得到n+1(2+1)个分区，剩余的消费者分配到N(2)个分区

offset：
不同的 group + topic + partition 就有一个offset。
kafka在0.9版本之前consumer默认将offset保存在zookeeper中，
从0.9版本开始，consumer默认将offset保存在kafka一个内置的
topic中，该topic为_consumer_offset。