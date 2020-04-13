package com.gapache.ngrok.cli.callback;

import com.gapache.commons.model.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.util.HashMap;

/**
 * @author HuSen
 * create on 2020/4/13 9:46 上午
 */
@Slf4j
@AllArgsConstructor
public abstract class BaseFutureCallback implements FutureCallback<HttpResponse> {

    private String messageId;

    protected void setHeaders(HttpResponse httpResponse, ClientResponse clientResponse) {
        Header[] allHeaders = httpResponse.getAllHeaders();
        clientResponse.setHeaders(new HashMap<>(allHeaders.length));
        for (Header header : allHeaders) {
            if (clientResponse.getHeaders().containsKey(header.getName())) {
                clientResponse.getHeaders().put(header.getName(), clientResponse.getHeaders().get(header.getName()).concat("~~~~~~").concat(header.getValue()));
            } else {
                clientResponse.getHeaders().put(header.getName(), header.getValue());
            }
        }
        // 跨域
        clientResponse.getHeaders().put("Access-Control-Allow-Origin", "*");
    }

    @Override
    public void failed(Exception e) {
        log.error("请求发生异常.", e);
    }

    @Override
    public void cancelled() {
        log.info("cancelled message:{}", messageId);
    }
}
