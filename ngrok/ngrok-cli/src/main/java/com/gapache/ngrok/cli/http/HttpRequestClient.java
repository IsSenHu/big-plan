package com.gapache.ngrok.cli.http;

import com.gapache.ngrok.cli.handler.DestinationResponseHandler;
import com.gapache.ngrok.cli.handler.RequestClientHandler;
import com.gapache.ngrok.commons.ServerRequest;
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

import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/15 10:54 上午
 */
@Slf4j
@Setter
@Getter
public class HttpRequestClient {
    private final EventLoopGroup group;
    private final String host;
    private final int port;
    private final int connectionTimeout;
    private Bootstrap bootstrap;
    private volatile boolean started;
    private volatile boolean connected;
    private volatile ChannelHandlerContext connection;
    private ServerRequest message;
    private HttpClient httpClient;

    public HttpRequestClient(String host, int port, ServerRequest message, HttpClient httpClient, int connectionTimeout) {
        this.host = host;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.message = message;
        this.httpClient = httpClient;
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

    public void request() {
        if (connected && connection != null) {
            HttpRequest request =
                    new DefaultHttpRequest(HttpVersion.valueOf(message.getProtocolVersion()), HttpMethod.valueOf(message.getMethod()), message.getUri());

            List<Map.Entry<String, String>> headers = message.getHeaders();
            for (Map.Entry<String, String> entry : headers) {
                request.headers().add(entry.getKey(), entry.getValue());
            }

            byte[] bytes = message.getContent();
            HttpContent content = null;
            if (bytes != null) {
                 content = new DefaultHttpContent(connection.alloc().buffer(bytes.length).writeBytes(bytes));
            }

            connection.write(request);
            if (content != null) {
                connection.write(content);
            }
            connection.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }
    }

    public void start() {
        synchronized (this) {
            if (!started && bootstrap != null) {
                log.info("HttpRequestClient {} is starting", Thread.currentThread().getName());
                bootstrap.connect(host, port).syncUninterruptibly();
                started = true;
            }
        }
    }

    private ChannelHandler createChannelInitializer() {
        HttpRequestClient httpRequestClient = this;
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpClientCodec());
                pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 5));
                pipeline.addLast(new RequestClientHandler(httpRequestClient));
                pipeline.addLast(new DestinationResponseHandler(httpRequestClient, httpClient));
            }
        };
    }
}
