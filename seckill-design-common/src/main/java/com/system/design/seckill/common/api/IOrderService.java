package com.system.design.seckill.common.api;


import com.system.design.seckill.common.entity.SeckillOrder;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
public interface IOrderService {
    /**
     * 创建订单
     * @param skuId
     * @param userId
     * @return
     */
    SeckillOrder createOrder(long skuId, String userId);

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    SeckillOrder getOrderInfo(long orderId);

    /**
     * 更新订单状态
     * @param orderId
     * @param status
     */
    void updateOrderStatus(long orderId, String status);
}
