package com.gapache.ngrok.server.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.ClientInfo;
import com.gapache.commons.model.ClientResponse;
import com.gapache.commons.model.Message;
import com.gapache.commons.model.ServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

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
        if (request instanceof ClientResponse) {
            ClientResponse resp = (ClientResponse) request;
            log.info("ngrok 客户端响应数据:{}", resp);
            if (MESSAGE_ID_CONNECTION_MAP.containsKey(resp.getId())) {
                ChannelHandlerContext connection = MESSAGE_ID_CONNECTION_MAP.get(resp.getId());
                response(connection, resp.getBody(), resp.getHeaders());
            }
        } else if (request instanceof ClientInfo) {
            ClientInfo clientInfo = (ClientInfo) request;
            log.info("ngrok 客户端注册:{}", clientInfo);
            CONNECTION_MAP.put(clientInfo.getId(), ctx);
            ServerInfo serverInfo = new ServerInfo();
            serverInfo.setServerId(UUID.randomUUID().toString());
            response(ctx, serverInfo, null);
        } else {
            Message message = (Message) request;
            log.info("外部请求内部服务:{}", message);
            String clientId = StringUtils.substringBetween(message.getDestination(), "/");
            if (StringUtils.isNotEmpty(clientId) && CONNECTION_MAP.containsKey(clientId)) {
                ChannelHandlerContext clientConnection = CONNECTION_MAP.get(clientId);
                // 响应给客户端，让客户端帮忙转发到对应的内网上
                MESSAGE_ID_CONNECTION_MAP.put(message.getId(), ctx);
                response(clientConnection, message, null);
            } else {
                // 返回给请求方
                response(ctx, "channel " + clientId +" not found", null);
            }
        }
    }

    private void response(ChannelHandlerContext ctx, Object o, Map<String, String> headers) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpContent content = null;
        String body;
        if (o != null) {
            body = o instanceof String ? o.toString() : JSON.toJSONString(o);
            byte[] bytes = body.getBytes(CharsetUtil.UTF_8);
            if (bytes.length != 0) {
                content = new DefaultHttpContent(ctx.alloc().buffer(bytes.length).writeBytes(bytes));
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
            }
        }
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach((key, value) -> {
                String[] split = value.split("~~~~~~");
                for (String s : split) {
                    response.headers().add(key, s);
                }
            });
        } else {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
        }
        ctx.write(response);
        if (content != null) {
            ctx.write(content);
        }
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }
}
