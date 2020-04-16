package com.gapache.ngrok.server.tcp;

import com.gapache.ngrok.server.handler.TcpLogicHandler;
import com.gapache.ngrok.commons.TcpMessageDecoder;
import com.gapache.ngrok.server.http.HttpServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 9:42 上午
 */
@Slf4j
@Setter
@Getter
public class TcpServer extends HttpServer {

    private volatile ChannelHandlerContext clientConnection;
    private volatile ChannelHandlerContext ngrokCliConnection;

    public TcpServer(int port, String name) {
        super(port, name);
    }

    @Override
    protected void doInitLogic(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new TcpMessageDecoder());
        pipeline.addLast(new TcpLogicHandler(this));
    }
}
