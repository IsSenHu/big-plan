package com.gapache.ngrok.cli.http;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.ngrok.cli.handler.HttpResponseHandler;
import com.gapache.ngrok.cli.handler.LifeCycleHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/11 3:32 下午
 */
@Slf4j
@Getter
@Setter
public class HttpClient {
    private final EventLoopGroup group;
    private final String host;
    private final int port;
    private final int localPort;
    private final String name;
    private final int connectionTimeout;
    private Bootstrap bootstrap;
    private volatile boolean started;
    private volatile boolean connected;
    private volatile boolean retry;
    private volatile ChannelHandlerContext connection;

    public HttpClient(String host, int port, int localPort, String name, int connectionTimeout) {
        this.host = host;
        this.port = port;
        this.localPort = localPort;
        this.name = name;
        this.connectionTimeout = connectionTimeout;
        group = new NioEventLoopGroup();
    }

    public void init() {
        bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout);
        bootstrap.handler(createChannelInitializer());
    }

    public void start() {
        synchronized (this) {
            if (!started && bootstrap != null) {
                log.info("HttpClient {} {} is starting", Thread.currentThread().getName(), name);
                bootstrap.connect(host, port).syncUninterruptibly();
                started = true;
            }
        }
    }

    public synchronized void retryConnect() {
        retry = false;
        init();
        if (!connected && bootstrap != null) {
            log.info("HttpClient {} {} is retryConnecting", Thread.currentThread().getName(), name);
            bootstrap.connect(host, port).syncUninterruptibly();
            started = true;
        }
    }

    public void request(Object o, Map<String, Object> headers) {
        String string;
        if (o instanceof String) {
            string = o.toString();
        } else {
            string = JSON.toJSONString(o);
        }
        byte[] bytes = IStringUtils.getBytes(string);
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "");
        httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach((key, value) -> httpRequest.headers().add(key, value));
        }
        HttpContent httpContent = new DefaultHttpContent(connection.alloc().buffer(bytes.length).writeBytes(bytes));
        connection.write(httpRequest);
        connection.write(httpContent);
        connection.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    private ChannelHandler createChannelInitializer() {
        LifeCycleHandler lifeCycleHandler = new LifeCycleHandler(host, port, name, this);
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpClientCodec());
                pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 5));
                pipeline.addLast(lifeCycleHandler);
                pipeline.addLast(new HttpResponseHandler(lifeCycleHandler.getClient()));
            }
        };
    }

    public synchronized boolean isRetry() {
        return retry;
    }

    public synchronized void setRetry(boolean retry) {
        this.retry = retry;
    }
}
