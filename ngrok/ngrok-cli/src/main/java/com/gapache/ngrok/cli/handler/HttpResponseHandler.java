package com.gapache.ngrok.cli.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.utils.HttpUtils;
import com.gapache.commons.model.Message;
import com.gapache.ngrok.cli.Constants;
import com.gapache.ngrok.cli.callback.DefaultCallback;
import com.gapache.ngrok.cli.http.HttpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

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
        this.innerAddress = "http://" + client.getInnerAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) {
        ByteBuf content = response.content();
        String resp = content.toString(CharsetUtil.UTF_8);
        JSONObject jsonObject = JSON.parseObject(resp);
        if (jsonObject.containsKey(Constants.SERVER_ID)) {
            return;
        }
        Message message = JSON.parseObject(resp, Message.class);
        log.info("message:{}, {}", message.getMethod(), message.getDestination());
        String clientId = "/" + client.getName();
        String url = innerAddress.concat(":").concat(String.valueOf(innerPort)).concat(StringUtils.substring(message.getDestination(), clientId.length()));
        Map<String, String> headers = message.getHeaders();
        switch (message.getMethod()) {
            case "options": {
                HttpUtils.optionsAsync(url, headers, new DefaultCallback(client, message.getId()));
                break;
            }
            case "get": {
                HttpUtils.getAsync(url, headers, new DefaultCallback(client, message.getId()));
                break;
            }
            case "post": {
                HttpUtils.postAsync(url, message.getBody(), headers, new DefaultCallback(client, message.getId()));
                break;
            }
            case "delete": {
                HttpUtils.deleteAsync(url, headers, new DefaultCallback(client, message.getId()));
                break;
            }
            case "put": {
                HttpUtils.putAsync(url, message.getBody(), headers, new DefaultCallback(client, message.getId()));
                break;
            }
            default:
        }
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
