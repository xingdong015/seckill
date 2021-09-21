package com.system.design.seckill.service;

import com.system.design.seckill.dbservice.OrderService;
import com.system.design.seckill.dbservice.SeckillService;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.job.ScheduleJob;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
public class OrderBuzService {
    @Autowired
    private OrderService   orderService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private ScheduleJob    scheduleJob;

    /**
     * 此处是从消息队列获取的秒杀成功消息记录
     *
     * @param killId
     * @param userId
     */
    public Long createOrder(long killId, String userId) throws NotFoundException {
        //扣减库存、锁定库存
        Boolean success = seckillService.deduct(killId);
        if (success) {
            Long orderId = orderService.createOrder(killId, userId);
            scheduleJob.addTask(30, () -> {
                Order orderInfo = orderService.getOrderInfo(orderId);
                if ("0".equals(orderInfo.getStatus())) {
                    payFail(orderInfo.getOrderId(), orderInfo.getSeckillId());
                }
            });
            pay(orderId, userId);
            return orderId;
        } else {
            seckillService.incCount(killId, 1);
            return null;
        }
    }

    public void pay(long orderId, String userId) throws NotFoundException {
        Order orderInfo = orderService.getOrderInfo(orderId);
        if (orderInfo == null) {
            throw new NotFoundException("order not found");
        }
        boolean success = doPay(orderInfo, userId);
        if (success) {
            paySuccess(orderInfo.getOrderId(), orderInfo.getProductId());
        } else {
            payFail(orderId, orderInfo.getSeckillId());
        }

    }

    private void payFail(long orderId, long seckillId) {
        //支付失败、需要将 redis的库存添加1、并且将此商品用户的购买集合删除掉、使用lua脚本保证原子性
        orderService.updateOrderStatus(orderId, "-1");
        seckillService.incCount(seckillId, 1);
    }

    private void paySuccess(long orderId, Long productId) {
        seckillService.deduct(productId);
        orderService.updateOrderStatus(orderId, "1");
    }

    private boolean doPay(Order orderInfo, String userId) {
        System.out.println(userId + " 开始支付成功,订单id " + orderInfo.getOrderId());
        return true;
    }
}
