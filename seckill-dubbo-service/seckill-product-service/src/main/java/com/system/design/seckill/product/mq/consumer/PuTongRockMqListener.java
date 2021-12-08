//package com.system.design.seckill.product.mq.consumer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Component;
//
///**
// * @description: 监听发送的普通消息
// * @author: 贾凯
// * @create: 2021-11-22 17:12
// */
//@Slf4j
//@Component
//@RocketMQMessageListener(topic = "JK-Topic",consumerGroup = "product-group-consumer111")
//public class PuTongRockMqListener implements RocketMQListener<String> {
//
//    @Override
//    public void onMessage(String msg) {
//        log.info("PuTongRockMqListener:消费者受到消息：{}", msg);
//    }
//}
