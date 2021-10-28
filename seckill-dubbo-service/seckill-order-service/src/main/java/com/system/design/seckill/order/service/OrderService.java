package com.system.design.seckill.order.service;

import com.google.common.base.Preconditions;
import com.system.design.seckill.common.api.IStockService;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.exception.SeckillException;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Objects;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
@DubboService
public class OrderService implements IOrderService {

    @Override
    public OrderEntity createOrder(long skuId, String userId) {
        return null;
    }

    @Override
    public OrderEntity getOrderInfo(long orderId) {
        return null;
    }

    @Override
    public void updateOrderStatus(long orderId, String s) {

    }

}
