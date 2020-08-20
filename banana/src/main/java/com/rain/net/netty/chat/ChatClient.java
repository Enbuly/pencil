package com.rain.net.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * ChatClient
 *
 * @author lazy cat
 * 2020-08-12
 **/
public class ChatClient {

    public static void main(String[] args) {
        String name = "";
        System.out.println("please enter you name:");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            name = scanner.next();
        }
        new ChatClient().start(name);
    }

    private void start(String name) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast("handler", new ChatClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
            Channel channel = future.channel();
            System.out.println("let us chat --->");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                channel.writeAndFlush("ChatClient " + name + ": " + in.readLine() + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
