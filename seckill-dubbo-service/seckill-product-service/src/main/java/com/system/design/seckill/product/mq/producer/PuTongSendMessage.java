package com.system.design.seckill.product.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 普通消息，分为三中发送方式
 * @author: 贾凯
 * @create: 2021-11-22 16:56
 */
@Slf4j
@Component
public class PuTongSendMessage {
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    private static final String TOPIC = "JK-Topic";

    //1.发送同步消息
    public void SyncSend(){
        SendResult sendResult = rocketMQTemplate.syncSend(TOPIC, "this is my first sync message");
        log.info("sendResult: {}", sendResult);
    }

    //2.发送异步消息
    public void AsyncSend(){
        rocketMQTemplate.asyncSend(TOPIC, "this is my second async message!!!", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("asyncSend send success");
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("asyncSend send failure");
            }
        });
    }

    //3.发送直接消息
    public void OneWaySend(){
        rocketMQTemplate.sendOneWay(TOPIC,"this is my third message!!!");
    }

}
