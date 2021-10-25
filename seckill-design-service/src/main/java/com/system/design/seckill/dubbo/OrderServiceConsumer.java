package com.system.design.seckill.dubbo;

import com.system.design.seckill.dubbo.service.IOrderService;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Slf4j
@EnableDiscoveryClient
public class OrderServiceConsumer {

    @Resource
    OrderMapper orderMapper;

    @DubboReference
    private IOrderService orderService;

    /**
     * 下单操作
     *
     * @param skuId  秒杀的商品id
     * @param userId 用户id
     * @return
     */
    public OrderEntity createOrder(long skuId, String userId) {
        return orderService.createOrder(skuId, userId);
    }

    /**
     * 获取订单详情数据
     *
     * @param orderId
     * @return
     */
    public OrderEntity getOrderInfo(Long orderId) {
        return orderMapper.findOrder(orderId);
    }

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param status
     */
    public void updateOrderStatus(long orderId, String status) {

    }
}
