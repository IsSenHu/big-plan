package com.gapache.cloud.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/6/12 9:59 上午
 */
@Slf4j
@Component
@EnableBinding(Sink.class)
public class MessageListener {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        log.info("{} 接收到消息:{}", serverPort, message.getPayload());
    }
}
