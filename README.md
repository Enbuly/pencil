### 前置条件
1、本项目使用jdk1.8版本  
2、lombok插件（232.10227.8）  
3、需要启动mysql（8.2.0） 、redis（6.0.18）、nacos（2.3.0）  
4、执行pencil/Adoc/笔记/pencil.sql  

### nacos访问地址
http://localhost:8848/nacos  

### 启动项目后，本地swagger访问地址
apple：http://localhost:8762/apple/swagger-ui.html  
banana：http://localhost:8763/banana/swagger-ui.html

## pencil note
#### apple
    AsyncController spring的异步任务demo
    GuavaLimitController guava的api级别限流demo
    LoginController 登陆controller
    RedisController redis使用demo，redis+lua使用，redis+lua布隆过滤器
    RemoteController 提供远程调用服务demo - RestTemplate远程调用提供者
    DubboServiceImpl dubbo远程调用提供者
    UserController 增删改查导入导出demo
    SecretDemoController post请求加密解密处理
#### banana(dubbo RestTemplate)
    DubboController dubbo远程调用demo
    EasyExcelController 演示了easy excel的导入导出(大数据的导入导出)
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
    演示spring boot yml数据库密码加密
#### gateway
    GatewayConfig gateway使用sentinel限流配置
    AuthFilter 鉴权
#### flower
    HelloController 为giraffe项目提供api演示openfeign调用
    演示了seata实现分布式事务
#### giraffe
    HiController 演示openfeign调用->调用flower的api
    演示了seata实现分布式事务