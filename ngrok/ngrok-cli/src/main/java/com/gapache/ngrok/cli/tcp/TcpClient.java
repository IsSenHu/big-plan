package com.gapache.ngrok.cli.tcp;

import com.gapache.ngrok.cli.handler.*;
import com.gapache.ngrok.cli.http.HttpClient;
import com.gapache.ngrok.commons.TcpMessageDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 10:23 上午
 */
@Slf4j
@Setter
@Getter
public class TcpClient extends HttpClient {

    private TcpRequestClient requestClient;

    public TcpClient(String host, int port, int innerPort, String innerAddress, String name, int connectionTimeout) {
        super(host, port, innerPort, innerAddress, name, connectionTimeout);
    }

    @Override
    protected ChannelHandler createChannelInitializer() {
        TcpLifeCycleHandler lifeCycleHandler = new TcpLifeCycleHandler(this);
        TcpServerMessageHandler tcpServerMessageHandler = new TcpServerMessageHandler(this);
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new TcpMessageDecoder());
                pipeline.addLast(lifeCycleHandler);
                pipeline.addLast(tcpServerMessageHandler);
            }
        };
    }
}
