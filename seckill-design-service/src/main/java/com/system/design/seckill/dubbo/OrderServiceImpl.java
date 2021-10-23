package com.system.design.seckill.dubbo;

import com.system.design.seckill.dao.OrderDao;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.dubbo.api.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
@Slf4j
@DubboService(version = "1.0",interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    /**
     * 下单操作
     *
     * @param skuId  秒杀的商品id
     * @param userId 用户id
     */
    @Override
    public Long createOrder(long skuId, String userId) {
        return orderDao.createOrder(skuId, userId);
    }

    /**
     * 获取订单详情数据
     *
     * @param orderId
     * @return
     */
    @Override
    public Order getOrderInfo(Long orderId) {
        return orderDao.getOrderInfo(orderId);
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
