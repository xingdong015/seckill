package com.system.design.seckill.common.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;


/**
 * @author : chengzhengbo
 * @description : Mq发送消息的类
 * @date: 2021-09-21 13:37:27
 */
@Slf4j
@Component
@Configuration
@EnableConfigurationProperties({RocketMQProperties.class})
@ConditionalOnProperty(prefix = "rocketmq", value = "isEnable", havingValue = "true")
public class RocketMQConfiguation {

    @Autowired
    private final RocketMQProperties properties;

    @Autowired
    private final ApplicationContext applicationContext;

    public RocketMQConfiguation(RocketMQProperties properties, ApplicationContext applicationContext) {
        this.properties         = properties;
        this.applicationContext = applicationContext;
    }

    /**
     * 注入一个默认的消费者
     *
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQProducer getRocketMQProducer() throws MQClientException {
        if (StringUtils.isEmpty(properties.getProducer().getGroup())) {
            throw new MQClientException(-1, "groupName is blank");
        }

        if (StringUtils.isEmpty(properties.getNameServer())) {
            throw new MQClientException(-1, "nameServerAddr is blank");
        }
        DefaultMQProducer producer = new DefaultMQProducer(properties.getProducer().getGroup());
        producer.setNamesrvAddr(properties.getNameServer());

        // 如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        producer.setMaxMessageSize(properties.getProducer().getMaxMessageSize());
        producer.setSendMsgTimeout(properties.getProducer().getSendMessageTimeout());
        // 如果发送消息失败，设置重试次数，默认为2次
        producer.setRetryTimesWhenSendFailed(properties.getProducer().getRetryTimesWhenSendFailed());

        try {
            producer.start();
            log.info("producer is start ! groupName:{},namesrvAddr:{}", properties.getProducer().getGroup(),
                    properties.getNameServer());
        } catch (MQClientException e) {
            log.error(String.format("producer is error %s %s", e.getMessage(), e));
            throw e;
        }
        return producer;
    }

    /**
     * SpringBoot启动时加载所有消费者
     */
    @PostConstruct
    public void initConsumer() {
        Map<String, AbstractRocketConsumer> consumers = applicationContext.getBeansOfType(AbstractRocketConsumer.class);
        if (consumers.size() == 0) {
            log.info("init rocket consumer 0");
        }
        for (String beanName : consumers.keySet()) {
            AbstractRocketConsumer consumer = consumers.get(beanName);
            consumer.init();
            createConsumer(consumer);
            log.info("init success consumer title {} , toips {} , tags {}", consumer.consumerTitle, consumer.tags, consumer.topics);
        }
    }

    /**
     * 通过消费者信心创建消费者
     *
     * @param arc
     */
    public void createConsumer(AbstractRocketConsumer arc) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.properties.getProducer().getGroup());
        consumer.setNamesrvAddr(this.properties.getNameServer());
        consumer.registerMessageListener(arc.messageListener);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        consumer.setMessageModel(MessageModel.CLUSTERING);

        /**
         * 设置一次消费消息的条数，默认为1条
         */
//        consumer.setConsumeMessageBatchMaxSize(this.properties.getConsumerConsumeMessageBatchMaxSize());
        try {
            consumer.subscribe(arc.topics, arc.tags);
            consumer.start();
            arc.mqPushConsumer = consumer;
        } catch (MQClientException e) {
            log.error("info consumer title {}", arc.consumerTitle, e);
        }

    }

}


