package com.system.design.seckill.order.service;

import com.google.common.base.Preconditions;
import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.common.exception.SeckillException;
import com.system.design.seckill.order.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
@DubboService(version = "1.0.0")
public class OrderService implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderEntity createOrder(long skuId, String userId) {
        System.out.println("创建订单成功....");
        return new OrderEntity();
    }

    @Override
    public OrderEntity getOrderInfo(long orderId) {

        return null;
    }

    @Override
    public void updateOrderStatus(long orderId, String s) {

    }


//    public Long doKill(long killId, String userId) {
//
//        int count = stockService.decreaseStorage(killId);
//        Preconditions.checkArgument(count >= 1, "%s|%s|库存不足", killId, userId);
//
//        OrderEntity order = orderService.createOrder(killId, userId);
//        if (Objects.isNull(order)) {
//            throw new SeckillException(String.format("order error => killId:%s userId:%s", killId, userId));
//        }
//        Preconditions.checkNotNull(order.getOrderId(), "%s|%s|订单创建失败", killId, userId);
//
//        addPayMonitor(order.getOrderId());
//
//        return order.getOrderId();
//    }

    private void addPayMonitor(Long orderId) {

    }

}
