package com.system.design.seckill.dbservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
public class OrderService extends ServiceImpl<OrderInfoMapper, Order> {
    public Long createOrder(long killId, String phone) {
        return null;
    }

    public Order getOrderInfo(long orderId) {
        return new Order();
    }

    public void updateOrderStatus(long orderId, String status) {

    }
}
