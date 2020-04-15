package com.gapache.ngrok.cli.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.ClientInfo;
import com.gapache.ngrok.cli.http.HttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 生命周期 Handler
 *
 * @author HuSen
 * create on 2019/7/5 12:51
 */
@Slf4j
@Getter
public class LifeCycleHandler extends ChannelInboundHandlerAdapter {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "retry connecting server"), new ThreadPoolExecutor.CallerRunsPolicy());

    private final String host;
    private final int port;
    private final String name;
    private final HttpClient client;

    public LifeCycleHandler(String host, int port, String name, HttpClient client) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.client = client;
    }

    /**
     * 1
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("channelRegistered:{}", this.name);
    }

    /**
     * 2
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 第一步：注册到ngrok server
        log.info("连接就绪:{} {}", this.name, Thread.currentThread().getName());
        client.setConnection(ctx);
        client.setConnected(true);
        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(() -> {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setId(this.name);
            Map<String, Object> header = new HashMap<>(1);
            header.put("x-register", "1");
            client.request(JSON.toJSONString(clientInfo), header);
        }, 0, 60, TimeUnit.SECONDS);
    }

    /**
     * 3
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("channelInactive:{}", this.name);
        if (client.isRetry()) {
            return;
        }
        client.setRetry(true);
        client.setConnected(false);
        SCHEDULED_EXECUTOR_SERVICE.schedule(client::retryConnect, 5L, TimeUnit.SECONDS);
    }

    /**
     * 4
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        log.info("channelUnregistered:{}", ctx);
        if (client.isRetry()) {
            return;
        }
        client.setRetry(true);
        client.setConnected(false);
        SCHEDULED_EXECUTOR_SERVICE.schedule(client::retryConnect, 5L, TimeUnit.SECONDS);
    }
}
