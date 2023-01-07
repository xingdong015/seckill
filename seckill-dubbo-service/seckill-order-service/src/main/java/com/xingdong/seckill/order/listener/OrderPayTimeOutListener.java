package com.xingdong.seckill.order.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.xingdong.seckill.common.po.SeckillOrder;
import com.xingdong.seckill.order.mapper.OrderMapper;
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
@RocketMQMessageListener(topic = "orderPayStatusMonitor", consumerGroup = "orderPayStatusMonitorGroup")
@Component
@Slf4j
public class OrderPayTimeOutListener implements RocketMQListener<String> {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private OrderMapper      orderMapper;

    @Override
    public void onMessage(String body) {
        //1. check订单的支付状态、如果是为支付状态、则取消订单。 使用rocketmq的分布式事务
        //向MQ发消息（申请退款）  out_trade_no（订单号）  out_refund_no（退款订单号）  total_fee（订单金额）  refund_fee（退款金额）
        log.info("接收到订单超时未支付消息: {}", body);
        SeckillOrder seckillOrder = JSON.parseObject(body, SeckillOrder.class);
        handlePayStatusExpired(seckillOrder);
    }

    private void handlePayStatusExpired(SeckillOrder seckillOrder) {
        Long         orderId   = seckillOrder.getOrderId();
        SeckillOrder orderInfo = orderMapper.selectById(orderId);
        if (orderInfo.getStatus() < 1) {
            //订单还未支付成功  事务消息参考 https://github.com/apache/rocketmq-spring/wiki/%E4%BA%8B%E5%8A%A1%E6%B6%88%E6%81%AF
            TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("orderStatusChange", MessageBuilder.withPayload(JSON.toJSONString(orderInfo)).build(), null);
            Preconditions.checkState(transactionSendResult.getSendStatus() == SendStatus.SEND_OK, "发送事务消息失败.");
        } else {
            //订单已经成功支付了
            log.info("receive order has pay success order: {}", orderInfo.getOrderId());
        }
    }


}
