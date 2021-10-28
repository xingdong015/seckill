package com.system.design.seckill.common.api;


import com.system.design.seckill.common.entity.OrderEntity;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
public interface IOrderService {
    OrderEntity createOrder(long skuId, String userId);

    @GlobalTransactional(rollbackFor = Exception.class)
    Long doKill(long killId, String userId);

    OrderEntity getOrderInfo(long orderId);

    void updateOrderStatus(long orderId, String s);
}
