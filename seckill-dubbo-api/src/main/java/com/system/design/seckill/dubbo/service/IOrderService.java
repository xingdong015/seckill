package com.system.design.seckill.dubbo.service;


import com.system.design.seckill.common.entity.OrderEntity;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
public interface IOrderService {
    OrderEntity createOrder(long skuId, String userId);
}
