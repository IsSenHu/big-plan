package com.gapache.ngrok.cli.callback;

import com.gapache.commons.model.ClientResponse;
import com.gapache.ngrok.cli.http.HttpClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/13 9:42 上午
 */
@Setter
@Slf4j
public class DefaultCallback extends BaseFutureCallback {

    private HttpClient client;

    public DefaultCallback(HttpClient client, String messageId) {
        super(messageId);
        this.client = client;
    }

    @Override
    public void completed(HttpResponse httpResponse) {
        try {
            log.info("completed message:{}", messageId);
            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setId(messageId);
            setHeaders(httpResponse, clientResponse);
            clientResponse.setBody(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
            Map<String, Object> requestHeaders = new HashMap<>(1);
            requestHeaders.put("x-ngrok", "1");
            final int maxShow = 100;
            if (StringUtils.length(clientResponse.getBody()) < maxShow) {
                log.info("body:{}", clientResponse.getBody());
            }
            client.request(clientResponse, requestHeaders);
        } catch (IOException e) {
            log.error("completed message error.", e);
        }
    }
}
