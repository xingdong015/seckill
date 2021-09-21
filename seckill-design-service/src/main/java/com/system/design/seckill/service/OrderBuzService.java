package com.system.design.seckill.service;

import com.system.design.seckill.bean.PayResultStatus;
import com.system.design.seckill.dbservice.OrderService;
import com.system.design.seckill.dbservice.SeckillService;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.job.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
@Slf4j
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
    public Long createOrder(long killId, String userId) {
        //扣减库存、锁定库存
        Boolean success = seckillService.deductStock(killId);
        if (success) {
            Long orderId = orderService.createOrder(killId, userId);
            scheduleJob.addTask(30, () -> {
                Order orderInfo = orderService.getOrderInfo(orderId);
                if ("0".equals(orderInfo.getStatus())) {
                    payFail(orderInfo.getOrderId(), orderInfo.getSeckillId());
                }
            });
            return orderId;
        } else {
            //这里失败的原因如下： 由于有可能消息投递过程中会出现部分失败的情况下，导致redis中扣减了库存而数据库却没有扣减库存
            //所以通常来说会给redis的库存中多放1.5倍的量、这样情况下，进入到mysql的有可能会大于实际的库存量。所以mysql中需要在通过
            //乐观锁的形式来判断库存是否小于0了

            //扣减库存失败。说明已经卖光了、此时无需在给redis的库存+1，直接返回秒杀失败就行。
            return null;
        }
    }

    public PayResultStatus pay(long orderId, long userId) {
        try{
            Order orderInfo = orderService.getOrderInfo(orderId);
            if (orderInfo == null) {
                return PayResultStatus.buildOrderExistedException(orderId, userId);
            }
            boolean success = doPay(orderInfo, userId);
            if (success) {
                orderService.updateOrderStatus(orderId, "1");
                return PayResultStatus.buildSuccessPay(orderId, userId);
            }
            payFail(orderId, orderInfo.getSeckillId());
            return PayResultStatus.buildPayFail(orderId, userId);
        }catch (Throwable e){
            log.error("OrderBuzService#pay error {} {}",orderId,userId,e);
            return PayResultStatus.buildException(orderId, userId);
        }
    }

    private void payFail(long orderId, long seckillId) {
        //支付失败、需要将 redis的库存添加1、并且将此商品用户的购买集合删除掉、使用lua脚本保证原子性
        orderService.updateOrderStatus(orderId, "-1");
        seckillService.incCount(seckillId, 1);
    }
    private boolean doPay(Order orderInfo, long userId) {
        if (new Random().nextInt(100) < 50) {
            System.out.println(userId + " 支付成功,订单id " + orderInfo.getOrderId());
            return true;
        }
        System.out.println(userId + " 支付失败,订单id " + orderInfo.getOrderId());
        return false;
    }
}
