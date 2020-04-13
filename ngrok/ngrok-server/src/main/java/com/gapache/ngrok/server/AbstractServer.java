package com.gapache.ngrok.server;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.ThreadFactory;

/**
 * @author HuSen
 * create on 2019/7/24 9:38
 */
@Slf4j
@Setter
@Getter
public abstract class AbstractServer {

    private volatile boolean initialized = false;
    private String bossThreadName;
    private String workerThreadName;
    private Integer bossThreads;
    private Integer workerThreads;
    private Boolean useEpoll;
    private Integer writeTimeout;
    private String name;
    private Integer port;

    public AbstractServer(String name, int port) {
        Assert.hasText(name, "not set name of instance");
        this.name = name;
        this.port = port;
        this.bossThreadName = String.format("[%s] %s", name, "boss-thread");
        this.workerThreadName = String.format("[%s] %s", name, "io-worker-thread");
    }

    private void initialize() {
        Assert.notNull(bossThreads, "bossThreads must be set");
        Assert.notNull(workerThreads, "workerThreads must be set");
        Assert.notNull(useEpoll, "useEpoll must be set");
        Assert.notNull(writeTimeout, "writeTimeout must be set");

        if (initialized) {
            return;
        }
        initialized = true;
        initBootstrap();
    }

    /**
     * 初始化Bootstrap
     */
    protected abstract void initBootstrap();

    protected ChannelHandler createChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                doInitTimeout(ch);
                doInitLogic(ch);
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        if (cause instanceof WriteTimeoutException) {
                            writeTimeout(ctx, cause);
                        } else {
                            exceptionCaught0(ctx, cause);
                        }
                    }
                });
            }
        };
    }

    /**
     * 异常处理
     *
     * @param ctx   ChannelHandlerContext
     * @param cause 异常
     */
    protected abstract void exceptionCaught0(ChannelHandlerContext ctx, Throwable cause);

    /**
     * 写超时处理
     *
     * @param ctx   ChannelHandlerContext
     * @param cause 异常
     */
    protected abstract void writeTimeout(ChannelHandlerContext ctx, Throwable cause);

    /**
     * 初始化超时处理
     *
     * @param ch SocketChannel
     */
    protected abstract void doInitTimeout(SocketChannel ch);

    /**
     * 执行初始化的逻辑
     *
     * @param ch SocketChannel
     */
    protected abstract void doInitLogic(SocketChannel ch);

    protected boolean trafficLog() {
        return true;
    }

    public void start() {
        if (!initialized) {
            initialize();
        }
        doStart();
    }

    /**
     * start server real logic
     */
    protected abstract void doStart();

    public void stop(Runnable callback) {
        if (callback != null) {
            callback.run();
        }
    }

    protected ThreadFactory newThreadFactory(String poolName) {
        return new DefaultThreadFactory(poolName);
    }
}
