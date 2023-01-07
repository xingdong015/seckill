package com.xingdong.seckill.order.listener;

import com.xingdong.seckill.common.bean.RocketMqMessageBean;
import com.xingdong.seckill.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 * <p>
 * spring boot starter rocketmq 接受消息 具体可以参考
 * https://github.com/apache/rocketmq-spring/wiki/%E6%8E%A5%E6%94%B6%E6%B6%88%E6%81%AF
 *
 * rocketmq 推消息模式、如果要采用pull形式需要配置
 * rocketmq.consumer.group=my-group1
 * rocketmq.consumer.topic=test
 *
 * 需要在本地启动 rocketmq
 * https://rocketmq.apache.org/docs/filter-by-sql92-example/
 */
@RocketMQMessageListener(topic = "redisKillSuccess", consumerGroup = "redisKillSuccessGroup")
@Component
@Slf4j
public class OrderListener implements RocketMQListener<RocketMqMessageBean> {
    @Resource
    private OrderService orderService;

    @Override
    public void onMessage(RocketMqMessageBean message) {
        String body = message.getBody();
        if (StringUtils.isNotBlank(body)) {
            try {
                String[] orderMsg  = StringUtils.split("|");
                String   killIdStr = orderMsg[0];
                String   userIdStr = orderMsg[1];
                orderService.doKill(Integer.parseInt(killIdStr), userIdStr);
            } catch (Exception e) {
                log.error("orderService#doKill error. {}", body, e);
            }
        }
    }
}
