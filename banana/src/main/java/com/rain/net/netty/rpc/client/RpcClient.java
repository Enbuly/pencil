package com.rain.net.netty.rpc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rain.net.netty.rpc.common.model.Request;
import com.rain.net.netty.rpc.common.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * RpcClient
 *
 * @author lazy cat
 * @since 2020-08-20
 **/
@Component
@ChannelHandler.Sharable
public class RpcClient extends SimpleChannelInboundHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);
    private final Object obj = new Object();
    private Response response;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        this.response = JSON.parseObject(msg, Response.class);

        synchronized (obj) {
            obj.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }

    Response send(Request request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            channel.pipeline()
                                    .addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringEncoder())
                                    .addLast(RpcClient.this);
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect("127.0.0.1", 8899).sync();
            future.channel().writeAndFlush(JSONObject.toJSONString(request)).sync();

            synchronized (obj) {
                obj.wait();
            }

            if (response != null) {
                future.channel().closeFuture();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }
}
