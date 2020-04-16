package com.gapache.ngrok.cli.handler;

import com.gapache.commons.utils.IStringUtils;
import com.gapache.ngrok.cli.tcp.TcpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 10:26 上午
 */
@Slf4j
public class TcpLifeCycleHandler extends ChannelInboundHandlerAdapter {

    private final TcpClient client;

    public TcpLifeCycleHandler(TcpClient client) {
        this.client = client;
    }

    private static final byte[] REGISTER = IStringUtils.getBytes("ngrok-cli-register");

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        client.setConnected(true);
        client.setConnection(ctx);
        ctx.writeAndFlush(ctx.alloc().buffer(REGISTER.length).writeBytes(REGISTER));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }
}
