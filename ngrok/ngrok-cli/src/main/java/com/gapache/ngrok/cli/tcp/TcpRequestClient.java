package com.gapache.ngrok.cli.tcp;
import com.gapache.ngrok.cli.handler.TcpRequestLifeCycleHandler;
import com.gapache.ngrok.cli.handler.TcpResponseHandler;
import com.gapache.ngrok.commons.TcpMessageDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 10:35 上午
 */
@Slf4j
@Setter
@Getter
public class TcpRequestClient {

    private final EventLoopGroup group;
    private String host;
    private int port;
    private final String name;
    private final int connectionTimeout;
    private Bootstrap bootstrap;
    private volatile boolean started;
    private volatile boolean connected;
    private volatile boolean retry;
    private volatile ChannelHandlerContext connection;
    private final TcpClient tcpClient;

    public TcpRequestClient(String host, int port, String name, int connectionTimeout, TcpClient tcpClient) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.connectionTimeout = connectionTimeout;
        this.tcpClient = tcpClient;
        this.group = new NioEventLoopGroup();
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

    public void send(byte[] msg) {
        connection.writeAndFlush(connection.alloc().buffer(msg.length).writeBytes(msg));
    }

    private ChannelHandler createChannelInitializer() {
        TcpRequestLifeCycleHandler tcpRequestLifeCycleHandler = new TcpRequestLifeCycleHandler(this);
        TcpResponseHandler tcpResponseHandler = new TcpResponseHandler(this);
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new TcpMessageDecoder());
                pipeline.addLast(tcpRequestLifeCycleHandler);
                pipeline.addLast(tcpResponseHandler);
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
