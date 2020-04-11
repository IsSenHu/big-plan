package com.gapache.ngrok.server.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.ClientInfo;
import com.gapache.commons.model.Message;
import com.gapache.commons.model.ServerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author HuSen
 * create on 2019/7/23 14:52
 */
@Slf4j
public class LogicHandler extends SimpleChannelInboundHandler<Object> {

    private static final ConcurrentMap<String, ChannelHandlerContext> CONNECTION_MAP = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("请求连接已建立:{}", ctx.channel());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object request) {
        if (request instanceof String) {
            String resp = (String) request;
        } else if (request instanceof ClientInfo) {
            ClientInfo clientInfo = (ClientInfo) request;
            log.info("ngrok 客户端注册:{}", clientInfo);
            CONNECTION_MAP.putIfAbsent(clientInfo.getId(), ctx);
            response(ctx, new ServerInfo());
        } else {
            Message message = (Message) request;
        }
    }

    private void response(ChannelHandlerContext ctx, Object o) {
        String body = JSON.toJSONString(o);
        byte[] bytes = body.getBytes(CharsetUtil.UTF_8);
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
        ctx.write(response);
        HttpContent content = new DefaultHttpContent(ctx.alloc().buffer(bytes.length).writeBytes(bytes));
        ctx.write(content);
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }
}
