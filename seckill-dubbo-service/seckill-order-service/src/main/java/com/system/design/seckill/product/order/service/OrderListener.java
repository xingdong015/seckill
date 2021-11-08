package com.system.design.seckill.product.order.service;

import com.system.design.seckill.common.bean.RocketMqMessageBean;
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
 */
@RocketMQMessageListener(topic = "${spring.rocketmq.consumer.topic", consumerGroup = "${spring.rocketmq.consumer.group")
@Component
public class OrderListener implements RocketMQListener<RocketMqMessageBean> {
    @Resource
    private OrderService orderService;

    @Override
    public void onMessage(RocketMqMessageBean message) {
        String body = message.getBody();
        if (StringUtils.isNotBlank(body)) {
            String[] orderMsg  = StringUtils.split("|");
            String   killIdStr = orderMsg[0];
            String   userIdStr = orderMsg[1];
            try {
                orderService.doKill(Integer.parseInt(killIdStr), userIdStr);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
