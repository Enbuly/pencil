package com.rain.study.net.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.apache.commons.lang3.StringUtils;

/**
 * ChatServerHandler
 *
 * @author lazy cat
 * 2020-08-12
 **/
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private final ChannelGroup group;

    ChatServerHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        group.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        group.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        Channel incoming = ctx.channel();
        for (Channel channel : group) {
            if (channel != incoming) {
                channel.writeAndFlush(msg + "\n");
            } else {
                channel.writeAndFlush(msg + "\n");
            }
        }
    }
}
