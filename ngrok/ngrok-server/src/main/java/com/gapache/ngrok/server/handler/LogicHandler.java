package com.gapache.ngrok.server.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.ClientInfo;
import com.gapache.commons.model.ServerInfo;
import com.gapache.ngrok.commons.ClientResponse;
import com.gapache.ngrok.commons.ServerRequest;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author HuSen
 * create on 2019/7/23 14:52
 */
@Slf4j
public class LogicHandler extends SimpleChannelInboundHandler<Object> {

    private static final ConcurrentMap<String, ChannelHandlerContext> CONNECTION_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, ChannelHandlerContext> MESSAGE_ID_CONNECTION_MAP = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("请求连接已建立:{}", ctx.channel());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object request) {
        // 第十一步：目的服务器响应成功，将数据响应给真正的请求方
        if (request instanceof ClientResponse) {
            ClientResponse resp = (ClientResponse) request;
            String messageId = resp.getMessageId();
            log.info("ngrok 客户端响应数据:{}", messageId);
            if (MESSAGE_ID_CONNECTION_MAP.containsKey(messageId)) {
                ChannelHandlerContext connection = MESSAGE_ID_CONNECTION_MAP.get(messageId);
                response(connection, resp);
            }
            return;
        }

        // 第三步：客户端注册成功，并把消息返回给客户端
        if (request instanceof ClientInfo) {
            ClientInfo clientInfo = (ClientInfo) request;
            log.info("ngrok 客户端注册:{}", clientInfo);
            CONNECTION_MAP.put(clientInfo.getId(), ctx);
            ServerInfo serverInfo = new ServerInfo();
            serverInfo.setServerId(UUID.randomUUID().toString());
            response(ctx, JSON.toJSONString(serverInfo), true);
            return;
        }

        // 第六步：将ServerRequest响应给客户端
        ServerRequest message = (ServerRequest) request;
        log.info("外部请求内部服务:{}, {}", message.getMethod(), message.getUri());
        String clientId = parseClientId(message.getUri());
        if (StringUtils.isNotEmpty(clientId) && CONNECTION_MAP.containsKey(clientId)) {
            ChannelHandlerContext clientConnection = CONNECTION_MAP.get(clientId);
            // 响应给客户端，让客户端帮忙转发到对应的内网上
            MESSAGE_ID_CONNECTION_MAP.put(message.getId(), ctx);
            response(clientConnection, message);
        } else {
            // 返回给请求方
            response(ctx, "channel " + clientId +" not found", false);
        }
    }

    private void response(ChannelHandlerContext ctx, String body, boolean returnClient) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpContent content = null;
        byte[] bytes = body.getBytes(CharsetUtil.UTF_8);
        if (bytes.length != 0) {
            content = new DefaultHttpContent(ctx.alloc().buffer(bytes.length).writeBytes(bytes));
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
        }
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
        if (returnClient) {
            response.headers().set("x-register", "1");
        }
        ctx.write(response);
        if (content != null) {
            ctx.write(content);
        }
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    private void response(ChannelHandlerContext ctx, ServerRequest serverRequest) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpContent content = null;
        byte[] bytes = ProtocstuffUtils.bean2Byte(serverRequest, ServerRequest.class);
        if (bytes != null && bytes.length > 0) {
            content = new DefaultHttpContent(ctx.alloc().buffer(bytes.length).writeBytes(bytes));
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
        }
        ctx.write(response);
        if (content != null) {
            ctx.write(content);
        }
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    private void response(ChannelHandlerContext ctx, ClientResponse clientResponse) {
        HttpVersion httpVersion = HttpVersion.valueOf(clientResponse.getProtocolVersion());
        HttpResponseStatus status = HttpResponseStatus.valueOf(clientResponse.getCode(), clientResponse.getReasonPhrase());
        HttpResponse response = new DefaultHttpResponse(httpVersion, status);

        List<Map.Entry<String, String>> headers = clientResponse.getHeaders();
        for (Map.Entry<String, String> header : headers) {
            response.headers().add(header.getKey(), header.getValue());
        }

        HttpContent content = null;
        byte[] bytes = clientResponse.getContent();
        if (bytes != null && bytes.length > 0) {
            content = new DefaultHttpContent(ctx.alloc().buffer(bytes.length).writeBytes(bytes));
        }

        ctx.write(response);
        if (content != null) {
            ctx.write(content);
        }
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    private String parseClientId(String uri) {
        String between = StringUtils.substringBetween(uri, "/");
        if (StringUtils.isNotBlank(between)) {
            return between;
        }
        return StringUtils.substringBetween(uri, "/", "?");
    }
}
