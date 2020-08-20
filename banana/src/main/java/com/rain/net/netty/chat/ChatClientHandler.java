package com.rain.net.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * ChatClientHandler
 *
 * @author lazy cat
 * 2020-08-12
 **/
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (!msg.contains("HTTP/1.1") && !msg.contains("Host:") && !msg.contains("Proxy-Connection: keep-alive") && !msg.contains("User-Agent:")) {
            System.out.println(msg);
        }
    }
}
