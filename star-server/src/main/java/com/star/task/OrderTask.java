package com.star.task;


import com.star.entity.Orders;
import com.star.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单相关的定时任务
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 取消超时订单的定时任务
     */
    @Scheduled(cron = "0 0/30 * * * ?") // 每30分钟执行一次
    public void cancelOvertimeOrders() {
        log.info("执行取消超时订单的定时任务");

        List<Orders> ordersList = orderMapper.getByStatusAndAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单超时未支付，自动取消");

                orderMapper.update(orders);
                log.info("取消订单：{}", orders.getNumber());
            }
        }
    }

    /**
     * 取消一直配送订单的定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行一次
    public void cancelOvertimeDeliveryOrders() {
        log.info("执行取消超时配送的定时任务");

        List<Orders> ordersList = orderMapper.getByStatusAndAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusMinutes(60));

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
