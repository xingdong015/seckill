package com.system.design.seckill.service;

import com.system.design.seckill.bean.PayResultStatus;
import com.system.design.seckill.bean.RocketMqMessageBean;
import com.system.design.seckill.dbservice.OrderService;
import com.system.design.seckill.dbservice.SeckillService;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.job.ScheduleJob;
import com.system.design.seckill.utils.JsonUtils;
import com.system.design.seckill.utils.KillEventTopiEnum;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
@Slf4j
public class OrderBuzService {
    @Autowired
    private OrderService      orderService;
    @Autowired
    private SeckillService    seckillService;
    @Autowired
    private ScheduleJob       scheduleJob;
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * 扣减库存、下单
     *
     * @param killId 秒杀的商品id
     * @param userId 用户id
     */
    @GlobalTransactional
    public Long createOrder(long killId, String userId) {
        //扣减库存、锁定库存
        seckillService.deductStock(killId);
        Long orderId = orderService.createOrder(killId, userId);
        scheduleJob.addTask(30, () -> {
            Order orderInfo = orderService.getOrderInfo(orderId);
            if ("0".equals(orderInfo.getStatus())) {
                Message message = new Message();
                message.setTopic(KillEventTopiEnum.PAY_STATUS_CHANGE.getTopic());
                RocketMqMessageBean bean = new RocketMqMessageBean(JsonUtils.objectToJson(orderId), -1, System.currentTimeMillis());
                message.setBody(JsonUtils.objectToJson(bean).getBytes(StandardCharsets.UTF_8));
                try {
                    defaultMQProducer.send(message);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        return orderId;

    }

    public PayResultStatus pay(long orderId, long userId) {
        try {
            Order orderInfo = orderService.getOrderInfo(orderId);
            if (orderInfo == null) {
                return PayResultStatus.buildOrderExistedException(orderId, userId);
            }
            boolean success = doPay(orderInfo, userId);
            if (success) {
                orderService.updateOrderStatus(orderId, "1");
                return PayResultStatus.buildSuccessPay(orderId, userId);
            }
            Message message = new Message();
            message.setTopic(KillEventTopiEnum.PAY_STATUS_CHANGE.getTopic());
            RocketMqMessageBean bean = new RocketMqMessageBean(JsonUtils.objectToJson(orderId), -1, System.currentTimeMillis());
            message.setBody(JsonUtils.objectToJson(bean).getBytes(StandardCharsets.UTF_8));
            defaultMQProducer.send(message);

            return PayResultStatus.buildPayFail(orderId, userId);
        }catch (Throwable e){
            log.error("OrderBuzService#pay error {} {}",orderId,userId,e);
            return PayResultStatus.buildException(orderId, userId);
        }
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
