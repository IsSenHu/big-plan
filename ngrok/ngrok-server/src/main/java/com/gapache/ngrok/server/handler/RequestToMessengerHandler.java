package com.gapache.ngrok.server.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.ClientInfo;
import com.gapache.commons.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2019/7/23 14:34
 */
public class RequestToMessengerHandler extends MessageToMessageDecoder<FullHttpRequest> {

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest request, List<Object> out) {
        HttpHeaders headers = request.headers();
        boolean fromInner = headers.contains("x-ngrok");
        boolean register = headers.contains("x-register");
        String body = request.content().toString(CharsetUtil.UTF_8);
        if (register) {
            ClientInfo clientInfo = JSON.parseObject(body, ClientInfo.class);
            out.add(clientInfo);
        } else if (fromInner) {
            out.add(body);
        } else {
            Message message = new Message();
            message.setMethod(request.method().name().toLowerCase());
            message.setDestination(request.uri());
            message.setHeaders(new HashMap<>(headers.size()));
            for (Map.Entry<String, String> header : headers) {
                message.getHeaders().put(header.getKey(), header.getValue());
            }
            message.setBody(body);
            out.add(message);
        }
    }
}
