package com.system.design.seckill.order.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.system.design.seckill.common.bean.RocketMqMessageBean;
import com.system.design.seckill.common.po.SeckillOrder;
import com.system.design.seckill.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/11/17
 */
@RocketMQMessageListener(topic = "orderPayStatusMonitor", consumerGroup = "business-group")
@Component
@Slf4j
public class PayStatusMonitorListener implements RocketMQListener<RocketMqMessageBean> {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private OrderMapper      orderMapper;

    @Override
    public void onMessage(RocketMqMessageBean message) {
        //1. check订单的支付状态、如果是为支付状态、则取消订单。 使用rocketmq的分布式事务
        //向MQ发消息（申请退款）  out_trade_no（订单号）  out_refund_no（退款订单号）  total_fee（订单金额）  refund_fee（退款金额）
        String body = message.getBody();
        log.info("receive order status expire time message: {}", body);
        SeckillOrder seckillOrder = JSON.parseObject(body, SeckillOrder.class);
        handlePayStatusExpired(seckillOrder);
    }

    private void handlePayStatusExpired(SeckillOrder seckillOrder) {
        Long         orderId   = seckillOrder.getOrderId();
        SeckillOrder orderInfo = orderMapper.selectById(orderId);
        if (orderInfo.getStatus() < 1) {
            //订单还未支付成功
            TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("cancelOrder", MessageBuilder.withPayload(orderId).build(), null);
            Preconditions.checkState(transactionSendResult.getSendStatus() == SendStatus.SEND_OK, "发送事务消息失败.");
        } else {
            //订单已经成功支付了
            log.info("receive order has pay success order: {}", orderInfo.getOrderId());
        }
    }
}