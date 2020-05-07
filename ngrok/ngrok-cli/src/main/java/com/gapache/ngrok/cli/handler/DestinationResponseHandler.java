package com.gapache.ngrok.cli.handler;

import com.gapache.ngrok.cli.http.HttpClient;
import com.gapache.ngrok.cli.http.HttpRequestClient;
import com.gapache.ngrok.commons.ClientResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/15 11:03 上午
 */
@Slf4j
public class DestinationResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private final HttpRequestClient requestClient;
    private final HttpClient httpClient;

    public DestinationResponseHandler(HttpRequestClient requestClient, HttpClient httpClient) {
        this.requestClient = requestClient;
        this.httpClient = httpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) {
        // 第九步：目的服务器响应了请求，封装成ClientResponse，并请求给ngrok 服务器
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setMessageId(requestClient.getMessage().getId());

        HttpResponseStatus status = response.status();
        clientResponse.setCode(status.code());
        clientResponse.setReasonPhrase(status.reasonPhrase());

        clientResponse.setProtocolVersion(response.protocolVersion().text());

        ByteBuf content = response.content();
        int readableBytes = content.readableBytes();
        byte[] bytes = null;
        if (readableBytes > 0) {
            bytes = new byte[readableBytes];
            content.getBytes(0, bytes);
        }
        clientResponse.setContent(bytes);

        HttpHeaders headers = response.headers();
        clientResponse.setHeaders(new ArrayList<>(headers.size()));
        for (Map.Entry<String, String> header : headers) {
            clientResponse.getHeaders().add(new AbstractMap.SimpleEntry<>(header.getKey(), header.getValue()));
        }

        HttpHeaders trailingHeaders = response.trailingHeaders();
        clientResponse.setTrailingHeaders(new ArrayList<>(trailingHeaders.size()));
        for (Map.Entry<String, String> header : trailingHeaders) {
            clientResponse.getTrailingHeaders().add(new AbstractMap.SimpleEntry<>(header.getKey(), header.getValue()));
        }

        Map<String, Object> singleMap = new HashMap<>(1);
        singleMap.put("x-ngrok", "1");
        httpClient.request(clientResponse, singleMap)
                .addListener(future -> {
                    ctx.close();
                    requestClient.getConnection().close();
                    requestClient.getGroup().shutdownGracefully();
                });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
