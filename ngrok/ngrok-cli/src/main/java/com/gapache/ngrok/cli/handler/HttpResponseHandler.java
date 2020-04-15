package com.gapache.ngrok.cli.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.ngrok.cli.Constants;
import com.gapache.ngrok.cli.http.HttpClient;
import com.gapache.ngrok.cli.http.HttpRequestClient;
import com.gapache.ngrok.commons.ServerRequest;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2019/7/23 17:26
 */
@Slf4j
public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private final HttpClient client;
    private final int innerPort;
    private final String innerAddress;

    public HttpResponseHandler(HttpClient client) {
        this.client = client;
        this.innerPort = client.getInnerPort();
        this.innerAddress = client.getInnerAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) {
        ByteBuf content = response.content();
        // 第四步：客户端接收到注册成功的消息
        boolean register = response.headers().contains("x-register");
        if (register) {
            String resp = content.toString(CharsetUtil.UTF_8);
            JSONObject jsonObject = JSON.parseObject(resp);
            if (jsonObject.containsKey(Constants.SERVER_ID)) {
                return;
            }
        }

        // 第七步：客户端接收到ServerRequest，绑定HttpRequestClient，并初始化和启动HttpRequestClient
        byte[] bytes = new byte[content.readableBytes()];
        content.getBytes(0, bytes);
        ServerRequest message = ProtocstuffUtils.byte2Bean(bytes, ServerRequest.class);
        log.info("message:{}, {}", message.getMethod(), message.getUri());
        String clientId = "/" + client.getName();
        String realUri = message.getUri().substring(clientId.length());
        message.setUri(realUri);
        HttpRequestClient requestClient = new HttpRequestClient(innerAddress, innerPort, message, client, 100);
        requestClient.init();
        requestClient.start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof ReadTimeoutException) {
            log.error("readTimeout for {}", ctx);
        } else {
            log.error("Request error:", cause);
        }
    }
}
