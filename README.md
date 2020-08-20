## pencil note
  #### apple
    AsyncController spring的异步任务demo
    GuavaLimitController guava的api级别限流demo
    LoginController 登陆controller
    RedisController redis使用demo，redis+lua使用，redis+lua布隆过滤器
    RemoteController 提供远程调用服务demo - RestTemplate远程调用提供者
    DubboServiceImpl dubbo远程调用提供者
    UserController 增删改查导入导出demo
  #### banana(dubbo RestTemplate)
    DubboController dubbo远程调用demo
    RestTemplateController RestTemplate远程调用demo
    net -> io,nio,netty -> netty rpc
  #### common
    MybatisConfig - mybatis plus配置
    FilterConfig XssFilter XssHttpServletRequestWrapper
    KafkaConfig
    LettuceRedisConfig
    RestTemplateConfig
    Swagger2
    ThreadPoolTaskExecutorConfig
    ResultCode
    BaseController
    exception - GlobalExceptionHandler
    ResultVo
    util
    ...
  #### cat(kafka zookeeper https)
    KafkaController kafka的使用demo
    LockController zookeeper的分布式锁demo，使用curator
    CuratorConfigurer
    ZookeeperConfigurer
    HttpConfig 使用https,同时支持http
    kafkaListener
    study -> sort,juc,lambda,steam,dom4j...
  #### dog(redis cluster)
    RedissonController redis集群下的分布式锁的获取
    StaticScheduleTask 定时任务
  #### elephant
    演示spring boot项目打成war包，并在tomcat发布
  #### gateway
    GatewayConfig gateway使用sentinel限流配置
    AuthFilter 鉴权
  #### flower
    HelloController 为giraffe项目提供api演示openfeign调用
    演示了seata实现分布式事务
  #### giraffe
    HiController 演示openfeign调用->调用flower的api
    演示了seata实现分布式事务