package com.gapache.ngrok.cli.handler;

import com.gapache.ngrok.cli.tcp.TcpRequestClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 10:42 上午
 */
@Slf4j
public class TcpRequestLifeCycleHandler extends ChannelInboundHandlerAdapter {

    private final TcpRequestClient client;

    public TcpRequestLifeCycleHandler(TcpRequestClient client) {
        this.client = client;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("TcpRequestLifeCycleHandler channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("TcpRequestLifeCycleHandler channelActive");
        client.setConnected(true);
        client.setConnection(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("TcpRequestLifeCycleHandler channelInactive");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        log.info("TcpRequestLifeCycleHandler channelUnregistered");
    }
}
