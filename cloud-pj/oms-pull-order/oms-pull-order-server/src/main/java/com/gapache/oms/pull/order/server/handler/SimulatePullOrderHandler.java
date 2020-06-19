package com.gapache.oms.pull.order.server.handler;

import com.gapache.oms.order.base.model.vo.order.*;
import com.gapache.oms.pull.order.server.message.OrderMessageProvider;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 模拟订单拉取
 *
 * @author HuSen
 * @since 2020/6/19 4:04 下午
 */
@Component
public class SimulatePullOrderHandler {

    @Resource
    private OrderMessageProvider orderMessageProvider;

    @Scheduled(fixedDelay = 5000L)
    public void start() {
        OrderBaseVO order = new OrderBaseVO();
        order.setOrderBn(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setCreateTime(LocalDateTime.now());
        order.setLastModifyTime(LocalDateTime.now());

        ConsigneeVO consignee = new ConsigneeVO();
        consignee.setName("胡森");
        consignee.setAreaState("四川省");
        consignee.setAreaCity("成都市");
        consignee.setAreaDistrict("双流区");
        consignee.setAddr("华阳镇街道天府大道南段金地天府城10栋2602");
        consignee.setMobile("");
        consignee.setTelephone("1178515826");
        order.setConsignee(consignee);

        List<OrderItemVO> items = new ArrayList<>();
        OrderItemVO item = new OrderItemVO();
        item.setName("无敌超级豪华大礼包");
        item.setBn("AJ887");
        item.setCount(2);
        item.setPrice(BigDecimal.valueOf(88));
        item.setAmount(item.getPrice().multiply(BigDecimal.valueOf(RandomUtils.nextInt(1, 5))));
        items.add(item);
        order.setItems(items);
        order.setCostItem(item.getAmount());

        ShipVO ship = new ShipVO();
        ship.setName("顺丰速递");
        ship.setIsCod(false);
        ship.setCost(BigDecimal.valueOf(10));
        ship.setOrderBn(order.getOrderBn());
        order.setShip(ship);

        order.setPmtPlatform(BigDecimal.valueOf(0));
        List<PmtDetailVO> pmtDetails = new ArrayList<>();
        PmtDetailVO pmtDetail = new PmtDetailVO();
        pmtDetail.setPmtAmount(BigDecimal.valueOf(20));
        pmtDetail.setPmtMemo("临时商家活动");
        pmtDetails.add(pmtDetail);
        order.setPmtDetails(pmtDetails);
        order.setPmtOrder(pmtDetail.getPmtAmount());
        order.setTotalAmount(order.getCostItem().add(ship.getCost()).subtract(order.getPmtOrder()));
        order.setCurAmount(order.getTotalAmount().subtract(order.getPmtPlatform()));

        order.setStatus(OrderStatus.TO_BE_DELIVERED);
        order.setPayed(order.getCurAmount());
        order.setPayStatus(PayStatus.PAID);

        orderMessageProvider.send(order);
    }
}
