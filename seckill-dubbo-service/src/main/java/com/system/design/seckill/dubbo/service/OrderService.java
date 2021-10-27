package com.system.design.seckill.dubbo.service;

import com.system.design.seckill.common.entity.OrderEntity;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
@DubboService
public class OrderService implements IOrderService {
    @Override
    public OrderEntity createOrder(long skuId, String userId) {
        System.out.println("创建订单成功");
        return new OrderEntity();
    }
    public OrderService(){
        System.out.println("创建OrderService。。。。。");
    }
}
