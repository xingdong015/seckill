package com.system.design.seckill.dubbo;

import com.system.design.seckill.entity.Order;
import com.system.design.seckill.dubbo.api.OrderService;
import com.system.design.seckill.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
@Slf4j
//@DubboService(version = "1.0",interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMapper orderMapper;

    /**
     * 下单操作
     *
     * @param skuId  秒杀的商品id
     * @param userId 用户id
     */
    @Override
    public Optional<Order> createOrder(long skuId, String userId) {
        Order order = new Order();
        order.setSeckillId(skuId);
        orderMapper.save(order);
        return Optional.of(order);
    }

    /**
     * 获取订单详情数据
     *
     * @param orderId
     * @return
     */
    @Override
    public Order getOrderInfo(Long orderId) {
        return orderMapper.findOrder(orderId);
    }

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param status
     */
    @Override
    public void updateOrderStatus(long orderId, String status) {

    }
}
