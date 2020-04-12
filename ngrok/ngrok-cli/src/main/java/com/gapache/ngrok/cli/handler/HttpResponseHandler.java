package com.gapache.ngrok.cli.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.model.ClientResponse;
import com.gapache.commons.utils.HttpUtils;
import com.gapache.commons.model.Message;
import com.gapache.ngrok.cli.http.HttpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2019/7/23 17:26
 */
@Slf4j
public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private final HttpClient client;
    private final int localPort;

    private static final String SERVER_ID = "serverId";
    private static final String LOCAL_IP = "http://127.0.0.1";

    public HttpResponseHandler(HttpClient client) {
        this.client = client;
        this.localPort = client.getLocalPort();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) {
        ByteBuf content = response.content();
        String resp = content.toString(CharsetUtil.UTF_8);
        JSONObject jsonObject = JSON.parseObject(resp);
        if (jsonObject.containsKey(SERVER_ID)) {
            return;
        }
        Message message = JSON.parseObject(resp, Message.class);
        log.info("message:{}, {}", message.getMethod(), message.getDestination());
        String clientId = "/" + client.getName();
        String url = LOCAL_IP.concat(":").concat(String.valueOf(localPort)).concat(StringUtils.substring(message.getDestination(), clientId.length()));
        Map<String, String> headers = message.getHeaders();
        switch (message.getMethod()) {
            case "options": {
                HttpUtils.optionsAsync(url, headers, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        ClientResponse clientResponse = new ClientResponse();
                        clientResponse.setId(message.getId());
                        setHeaders(httpResponse, clientResponse);
                        try {
                            clientResponse.setBody(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Map<String, Object> requestHeaders = new HashMap<>(1);
                        requestHeaders.put("x-ngrok", "1");
                        client.request(clientResponse, requestHeaders);
                    }

                    @Override
                    public void failed(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void cancelled() {

                    }
                });
                break;
            }
            case "get": {
                HttpUtils.getAsync(url, headers, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        ClientResponse clientResponse = new ClientResponse();
                        clientResponse.setId(message.getId());
                        setHeaders(httpResponse, clientResponse);
                        try {
                            clientResponse.setBody(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Map<String, Object> requestHeaders = new HashMap<>(1);
                        requestHeaders.put("x-ngrok", "1");
                        client.request(clientResponse, requestHeaders);
                    }

                    @Override
                    public void failed(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void cancelled() {

                    }
                });
                break;
            }
            case "post": {
                HttpUtils.postAsync(url, message.getBody(), headers, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        ClientResponse clientResponse = new ClientResponse();
                        clientResponse.setId(message.getId());
                        setHeaders(httpResponse, clientResponse);
                        try {
                            clientResponse.setBody(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Map<String, Object> requestHeaders = new HashMap<>(1);
                        requestHeaders.put("x-ngrok", "1");
                        client.request(clientResponse, requestHeaders);
                    }

                    @Override
                    public void failed(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void cancelled() {

                    }
                });
                break;
            }
            case "delete": {
                HttpUtils.deleteAsync(url, headers, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        ClientResponse clientResponse = new ClientResponse();
                        clientResponse.setId(message.getId());
                        setHeaders(httpResponse, clientResponse);
                        try {
                            clientResponse.setBody(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Map<String, Object> requestHeaders = new HashMap<>(1);
                        requestHeaders.put("x-ngrok", "1");
                        client.request(clientResponse, requestHeaders);
                    }

                    @Override
                    public void failed(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void cancelled() {

                    }
                });
                break;
            }
            case "put": {
                HttpUtils.putAsync(url, message.getBody(), headers, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        ClientResponse clientResponse = new ClientResponse();
                        clientResponse.setId(message.getId());
                        setHeaders(httpResponse, clientResponse);
                        try {
                            clientResponse.setBody(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Map<String, Object> requestHeaders = new HashMap<>(1);
                        requestHeaders.put("x-ngrok", "1");
                        client.request(clientResponse, requestHeaders);
                    }

                    @Override
                    public void failed(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
            default:
        }
    }

    private void setHeaders(HttpResponse httpResponse, ClientResponse clientResponse) {
        Header[] allHeaders = httpResponse.getAllHeaders();
        clientResponse.setHeaders(new HashMap<>(allHeaders.length));
        for (Header header : allHeaders) {
            if (clientResponse.getHeaders().containsKey(header.getName())) {
                clientResponse.getHeaders().put(header.getName(), clientResponse.getHeaders().get(header.getName()).concat("~~~~~~").concat(header.getValue()));
            } else {
                clientResponse.getHeaders().put(header.getName(), header.getValue());
            }
        }
        clientResponse.getHeaders().put("Access-Control-Allow-Origin", "*");
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
