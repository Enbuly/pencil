## 关于dubbo的一些总结
   #### 关于dubbo协议
     一些单词:
       1、protocol
       2、hessian
     一些协议
       1、dubbo(默认)协议：
       传输服务：mina,netty(默认),grizzy
       序列化：dubbo,hessian(默认),java,fastjson
       链接描述：单个长连接NIO,异步传输
       使用场景：1.常规RPC调用 2.传输数据量小 3.提供者少于消费者
       2、rmi协议
       传输服务：java rmi 服务
       序列化：java原生二进制序列化
       链接描述：多个短连接，BIO同步传输
       使用场景：1.常规RPC调用 2.与原RMI客户端集成 3.可传少量文件 4.不支持防火墙穿透
       3、hessian协议
       传输服务：servlet容器
       序列化：hessian二进制序列化
       链接描述：基于Http 协议传输，依懒servlet容器配置
       使用场景：1.提供者多于消费者 2.可传大字段和文件 3.跨语言调用
       4、http协议
       传输服务：servlet容器
       序列化：http表单
       链接描述：依懒servlet容器配置
       使用场景：1、数据包大小混合
   #### 关于负载均衡
       分别是基于权重随机算法的 RandomLoadBalance
       基于最少活跃调用数算法的 LeastActiveLoadBalance
       基于 hash 一致性的 ConsistentHashLoadBalance
       以及基于加权轮询算法的 RoundRobinLoadBalance