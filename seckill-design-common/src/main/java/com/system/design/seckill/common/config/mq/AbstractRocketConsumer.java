package com.system.design.seckill.common.config.mq;

import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;

/**
 * 消费者基本信息
 *
 * @author chengzhengbo
 * @date 2021-09-21 13:57:44
 */
public abstract class AbstractRocketConsumer implements RocketConsumer {

    protected String          topics;
    protected String          tags;
    protected MessageListener messageListener;
    protected String          consumerTitle;
    protected MQPushConsumer  mqPushConsumer;


    /**
     * 必要的信息
     *
     * @param topics
     * @param tags
     * @param consumerTitle
     */
    public void necessary(String topics, String tags, String consumerTitle) {
        this.topics = topics;
        this.tags = tags;
        this.consumerTitle = consumerTitle;
    }

    public abstract void init();

    @Override
    public void registerMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

}