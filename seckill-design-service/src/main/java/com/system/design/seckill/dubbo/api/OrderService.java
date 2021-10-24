package com.system.design.seckill.dubbo.api;

import com.system.design.seckill.entity.Order;

import java.util.Optional;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public interface OrderService {
    Optional<Order> createOrder(long skuId, String userId);

    Order getOrderInfo(Long orderId);

    void updateOrderStatus(long orderId, String status);
}
