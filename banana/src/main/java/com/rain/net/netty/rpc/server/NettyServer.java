package com.rain.net.netty.rpc.server;

import com.rain.net.netty.rpc.common.annotation.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * NettyServer
 *
 * @author lazy cat
 * 2020-08-20
 **/
@Component
public class NettyServer implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final String SERVER_ADDRESS = "127.0.0.1:8899";
    private Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : beans.values()) {
            Class<?> clazz = serviceBean.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> inter : interfaces) {
                String interfaceName = inter.getName();
                logger.info("加载服务类: {}", interfaceName);
                serviceMap.put(interfaceName, serviceBean);
            }
        }
        logger.info("已加载全部服务接口:{}", serviceMap);
    }

    @Override
    public void afterPropertiesSet() {
        start();
    }

    private void start() {
        final NettyServerHandler handler = new NettyServerHandler(serviceMap);
        new Thread(() -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup).
                        channel(NioServerSocketChannel.class).
                        option(ChannelOption.SO_BACKLOG, 1024).
                        childOption(ChannelOption.SO_KEEPALIVE, true).
                        childOption(ChannelOption.TCP_NODELAY, true).
                        childHandler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel channel) {
                                ChannelPipeline pipeline = channel.pipeline();
                                pipeline.addLast("decoder", new StringDecoder());
                                pipeline.addLast("encoder", new StringEncoder());
                                pipeline.addLast(handler);
                            }
                        });
                String[] array = SERVER_ADDRESS.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                ChannelFuture cf = bootstrap.bind(host, port).sync();
                logger.info("RPC 服务器启动.监听端口:" + port);
                cf.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();
    }
}
