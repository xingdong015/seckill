package com.system.design.seckill.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.system.design.seckill.bean.OrderStatusEnum;
import com.system.design.seckill.bean.RocketMqMessageBean;
import com.system.design.seckill.common.utils.KillEventTopiEnum;
import com.system.design.seckill.dubbo.api.OrderService;
import com.system.design.seckill.dubbo.api.StorageService;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.exception.SeckillException;
import com.system.design.seckill.job.ScheduleJob;
import com.system.design.seckill.service.api.OrderBuzService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public class OrderBuzServiceImpl implements OrderBuzService {
    @Autowired
    private OrderService      orderService;
    @Autowired
    private StorageService    storageService;
    @Autowired
    private ScheduleJob       scheduleJob;
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * 扣减库存、下单
     *
     * @param killId 秒杀的商品id
     * @param userId 用户id
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public Long doKill(long killId, String userId) {

        int count = storageService.reduceStock(killId);
        Preconditions.checkArgument(count >= 1, "%s|%s|库存不足", killId, userId);

        Optional<Order> orderOptional = orderService.createOrder(killId, userId);
        if (!orderOptional.isPresent()) {
            throw new SeckillException(String.format("order error => killId:%s userId:%s", killId, userId));
        }
        Order order = orderOptional.get();
        Preconditions.checkNotNull(order.getOrderId(), "%s|%s|订单创建失败", killId, userId);

        addPayMonitor(order.getOrderId());

        return order.getOrderId();
    }

    private void addPayMonitor(Long orderId) {
        scheduleJob.addTask(30, () -> {
            Order orderInfo = orderService.getOrderInfo(orderId);
            if (OrderStatusEnum.INIT.getStatus().equals(orderInfo.getStatus())) {
                Message message = new Message();
                message.setTopic(KillEventTopiEnum.PAY_STATUS_CHANGE.getTopic());
                JSONObject object = new JSONObject();
                object.put("orderId",orderId);
                RocketMqMessageBean bean = new RocketMqMessageBean(object.toJSONString(), -1,
                        System.currentTimeMillis());
                message.setBody(JSONObject.toJSONString(bean).getBytes(StandardCharsets.UTF_8));
                try {
                    defaultMQProducer.sendOneway(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
