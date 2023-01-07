package com.xingdong.seckill.product.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:顺序发送消息
 * @author: 贾凯
 * @create: 2021-11-22 17:09
 */
@Slf4j
@Component
public class ShunXuSendMessage {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private static final String TOPIC = "JK-Topic";
    private static final String HASH_KEY = "JK-Key";

    public void sendOrderly(){
        SendResult syncSendOrderly = rocketMQTemplate.syncSendOrderly(TOPIC, "同步顺序发送的消息", HASH_KEY);
        log.info("syncSendOrderly: {}",syncSendOrderly);
    }

    public void asyncSendOrderly(){
        rocketMQTemplate.asyncSendOrderly(TOPIC, "同步顺序发送的消息", HASH_KEY, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("asyncSendOrderly success");
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("asyncSendOrderly failure");
            }
        });
    }

}
