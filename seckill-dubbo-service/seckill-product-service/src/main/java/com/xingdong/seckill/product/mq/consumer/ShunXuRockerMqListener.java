package com.xingdong.seckill.product.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @description:顺序消费：
 * 转账等业务时需要保证消息顺序性，RocketMQ顺序性不是消费者保证，而是生产者保证。
 * 生产者发送消息时需要指定路由Key，RocketMQ会自动分配到同一MessageQueue中。
 * @author: 贾凯
 * @create: 2021-11-22 17:34
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "JK-Topic",consumerGroup = "product-group-consumer222",
                consumeMode = ConsumeMode.ORDERLY)
public class ShunXuRockerMqListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("ShunXuRockerMqListener msg: {}",s);
    }
}
