package com.system.design.seckill.service;

import com.alibaba.fastjson.JSONObject;
import com.system.design.seckill.bean.PayResultStatus;
import com.system.design.seckill.bean.RocketMqMessageBean;
import com.system.design.seckill.service.api.PayBuzService;
import com.system.design.seckill.common.utils.KillEventTopiEnum;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.dubbo.OrderServiceImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public class PayBuzServiceImpl implements PayBuzService {

    @Autowired
    private OrderServiceImpl  orderServiceImpl;
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Override
    public PayResultStatus pay(long orderId, long userId) {
        try {
            Order orderInfo = orderServiceImpl.getOrderInfo(orderId);
            if (orderInfo == null) {
                return PayResultStatus.buildOrderExistedException(orderId, userId);
            }
            boolean success = doPay(orderInfo, userId);
            if (success) {
                orderServiceImpl.updateOrderStatus(orderId, "1");
                return PayResultStatus.buildSuccessPay(orderId, userId);
            }
            Message message = new Message();
            message.setTopic(KillEventTopiEnum.PAY_STATUS_CHANGE.getTopic());
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            RocketMqMessageBean bean = new RocketMqMessageBean(object.toJSONString(), -1, System.currentTimeMillis());
            message.setBody(JSONObject.toJSONString(bean).getBytes(StandardCharsets.UTF_8));
            defaultMQProducer.send(message);

            return PayResultStatus.buildPayFail(orderId, userId);
        } catch (Throwable e) {
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
