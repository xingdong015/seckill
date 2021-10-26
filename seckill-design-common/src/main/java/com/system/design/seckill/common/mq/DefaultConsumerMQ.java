package com.system.design.seckill.common.mq;

import com.system.design.seckill.common.config.mq.AbstractRocketConsumer;
import com.system.design.seckill.common.utils.KillEventTopiEnum;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.stereotype.Component;

@Component
public class DefaultConsumerMQ extends AbstractRocketConsumer {

    /**
     * 初始化消费者
     */
    @Override
    public void init() {
        // 设置主题,标签与消费者标题
        super.necessary(KillEventTopiEnum.KILL_SUCCESS.getTopic(), "*", "order标题");
        //消费者具体执行逻辑
        registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            msgs.forEach(msg -> System.out.printf("consumer message boyd %s %n", new String(msg.getBody())));
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
    }
}