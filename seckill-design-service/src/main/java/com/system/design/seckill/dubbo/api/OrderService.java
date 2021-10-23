package com.system.design.seckill.dubbo.api;

import com.system.design.seckill.entity.Order;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public interface OrderService {
    Long createOrder(long skuId, String userId);

    Order getOrderInfo(Long orderId);

    void updateOrderStatus(long orderId, String status);
}
