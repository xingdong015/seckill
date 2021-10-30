package com.system.design.seckill.order.service;

import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.order.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
@DubboService
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

}
