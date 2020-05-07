package com.gapache.ngrok.server.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.ClientInfo;
import com.gapache.ngrok.commons.ClientResponse;
import com.gapache.ngrok.commons.ServerRequest;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author HuSen
 * create on 2019/7/23 14:34
 */
@Slf4j
public class RequestToMessengerHandler extends MessageToMessageDecoder<FullHttpRequest> {

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest request, List<Object> out) {
        HttpHeaders headers = request.headers();

        // 第二步：服务端接收客户端的注册
        boolean register = headers.contains("x-register");
        if (register) {
            String body = request.content().toString(CharsetUtil.UTF_8);
            ClientInfo clientInfo = JSON.parseObject(body, ClientInfo.class);
            out.add(clientInfo);
            return;
        }

        byte[] content = new byte[0];
        int readableBytes = request.content().readableBytes();
        if (readableBytes > 0) {
            content = new byte[readableBytes];
            request.content().getBytes(0, content);
        }

        // 第十步：接收真正的目的服务器的响应
        boolean fromInner = headers.contains("x-ngrok");
        if (fromInner) {
            ClientResponse clientResponse = ProtocstuffUtils.byte2Bean(content, ClientResponse.class);
            out.add(clientResponse);
            return;
        }

        // 第五步：接收到真正的Http请求，并将其封装为ServerRequest
        ServerRequest message = new ServerRequest();
        message.setId(UUID.randomUUID().toString());
        message.setMethod(request.method().name());
        message.setProtocolVersion(request.protocolVersion().text());
        message.setUri(request.uri());
        message.setHeaders(new ArrayList<>(headers.size()));
        for (Map.Entry<String, String> header : headers) {
            Map.Entry<String, String> item = new AbstractMap.SimpleEntry<>(header.getKey(), header.getValue());
            message.getHeaders().add(item);
        }

        HttpHeaders trailingHeaders = request.trailingHeaders();
        message.setTrailingHeaders(new ArrayList<>(trailingHeaders.size()));
        for (Map.Entry<String, String> header : trailingHeaders) {
            Map.Entry<String, String> item = new AbstractMap.SimpleEntry<>(header.getKey(), header.getValue());
            message.getTrailingHeaders().add(item);
        }
        message.setContent(content);
        out.add(message);
    }
}
