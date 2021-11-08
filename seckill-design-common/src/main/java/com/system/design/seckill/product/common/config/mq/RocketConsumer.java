package com.system.design.seckill.product.common.config.mq;

import org.apache.rocketmq.client.consumer.listener.MessageListener;

/**
 * 消费者接口
 *
 * @author chengzhengbo
 * @date 2021-09-21 13:57:19
 */
public interface RocketConsumer {

    /**
     * 初始化消费者
     */
    void init();

    /**
     * 注册监听
     *
     * @param messageListener
     */
    void registerMessageListener(MessageListener messageListener);

}