package com.gapache.ngrok.cli.handler;

import com.gapache.ngrok.cli.http.HttpRequestClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/15 11:20 上午
 */
@Slf4j
public class RequestClientHandler extends ChannelInboundHandlerAdapter {

    private HttpRequestClient client;

    public RequestClientHandler(HttpRequestClient client) {
        this.client = client;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 第八步：HttpRequestClient连接到目的服务器后，发送请求
        client.setConnection(ctx);
        client.setConnected(true);
        client.request();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("HttpRequestClient channelInactive");
        client.setConnected(false);
        client.setConnection(null);
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        client.setConnected(false);
        client.setConnection(null);
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("RequestClientHandler error.", cause);
        super.exceptionCaught(ctx, cause);
    }
}
