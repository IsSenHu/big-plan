package com.gapache.oms.pull.order.server.message.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;
import com.gapache.oms.pull.order.server.message.OrderMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/19 3:55 下午
 */
@Slf4j
@EnableBinding(Source.class)
public class OrderMessageProviderImpl implements OrderMessageProvider {

    /**
     * 消息发送管道
     */
    @Resource
    private MessageChannel output;

    @Override
    public void send(OrderBaseVO orderBase) {
        if (log.isDebugEnabled()) {
            log.debug("send order:{}", JSON.toJSONString(orderBase));
        }
        output.send(MessageBuilder.withPayload(orderBase).build());
    }
}
