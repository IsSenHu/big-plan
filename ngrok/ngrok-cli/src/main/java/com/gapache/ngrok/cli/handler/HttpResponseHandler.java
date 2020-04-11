package com.gapache.ngrok.cli.handler;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.commons.utils.OkHttpUtils;
import com.gapache.commons.model.Message;
import com.gapache.ngrok.cli.http.HttpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * @author HuSen
 * create on 2019/7/23 17:26
 */
@Slf4j
public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private final HttpClient client;

    public HttpResponseHandler(HttpClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) {
        ByteBuf content = response.content();
        String resp = content.toString(CharsetUtil.UTF_8);
        Message message = JSON.parseObject(resp, Message.class);
        if (message.getMethod() == null) {
            return;
        }
        log.info("message:{}", message);
        switch (message.getMethod()) {
            case "get": {
                OkHttpUtils.getAsync(message.getDestination(), null, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String string = Objects.requireNonNull(response.body()).string();
                        client.request(string, null);
                    }
                });
                break;
            }
            case "post":
            case "delete":
            case "put": {

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
