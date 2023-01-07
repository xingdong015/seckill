package com.xingdong.seckill.product.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @description: 发送事务消息
 * @author: 贾凯
 * @create: 2021-11-24 14:08
 */
@Slf4j
@Component
public class TransactionSendMessage {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private static final String TOPIC = "JK-Topic";
    public void send(){
        Message<String> message = MessageBuilder.withPayload("这是一条事务消息").build();
        rocketMQTemplate.sendMessageInTransaction(TOPIC,message,"topicSend");
    }

}
