package com.gapache.oms.split.order.server.message;

import com.alibaba.fastjson.JSON;
import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;
import com.gapache.oms.split.order.server.service.SplitOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/22 10:13 上午
 */
@Slf4j
@Component
@EnableBinding(Sink.class)
public class OrderMessageListener {

    @Resource
    private SplitOrderService splitOrderService;

    @StreamListener(Sink.INPUT)
    public void input(Message<OrderBaseVO> message) {
        if (log.isDebugEnabled()) {
            log.debug("接收到新订单:{}", JSON.toJSONString(message.getPayload()));
        }
        splitOrderService.autoSplitOrder(message.getPayload());
    }
}
