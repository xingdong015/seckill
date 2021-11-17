package com.system.design.seckill.order.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.enums.SeckillStatusEnum;
import com.system.design.seckill.common.exception.SeckillException;
import com.system.design.seckill.common.po.SeckillOrder;
import com.system.design.seckill.order.mapper.KillMapper;
import com.system.design.seckill.order.mapper.OrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
@DubboService(version = "1.0.0")
public class OrderService implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KillMapper       killMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 创建订单操作
     *
     * @param skuId
     * @param userId
     * @return
     */
    @Override
    public SeckillOrder createOrder(long skuId, String userId) {
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setSeckillId(skuId);
        seckillOrder.setUserId(userId);
        seckillOrder.setCreateTime(System.currentTimeMillis());
        seckillOrder.setUpdateTime(System.currentTimeMillis());
        seckillOrder.setStatus(0);
        int insert = orderMapper.insert(seckillOrder);
        Preconditions.checkArgument(insert > 0, "创建订单失败.");
        return seckillOrder;
    }

    /**
     * 获取订单详情信息
     * 注意给前台返回需要转换为VO对象
     *
     * @param orderId
     * @return
     */
    @Override
    public SeckillOrder getOrderInfo(long orderId) {
        return orderMapper.selectById(orderId);
    }

    /**
     * 更新订单状态
     * 支付成功： 更新订单状态为成功
     * 支付失败: 更新订单状态为失败。
     *
     * @param orderId
     * @param s
     */
    @Override
    public void updateOrderStatus(long orderId, String s) {
        int affect = orderMapper.updateStatus(orderId, s);
        Preconditions.checkArgument(affect >= 1, "更新状态失败!");
    }

    /**
     * 从business业务中获取的秒杀数据
     * 执行扣减库存。创建订单等逻辑。提交到rocketMq的延迟队列中去。
     * 分布式事务。事务消息机制
     * @param killId
     * @param userId
     * @return
     */
    @GlobalTransactional
    public Long doKill(long killId, String userId) throws MQClientException {
        //1. 扣减库存
        int count = killMapper.decreaseStorage(killId);
        Preconditions.checkArgument(count >= 1, "%s|%s|库存不足", killId, userId);

        //2. 创建订单
        SeckillOrder order = createOrder(killId, userId);
        if (Objects.isNull(order)) {
            throw new SeckillException(String.format("order error => killId:%s userId:%s", killId, userId), SeckillStatusEnum.REPEAT_KILL);
        }
        Preconditions.checkNotNull(order.getOrderId(), "%s|%s|订单创建失败", killId, userId);
        //添加到RocketMq的延迟消息当中去，监控订单的支付状态 事务消息
        Message message = new GenericMessage(JSON.toJSONString(order));

        TransactionListener   transactionListener = new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(org.apache.rocketmq.common.message.Message msg, Object arg) {
                return null;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                return null;
            }
        };
        TransactionMQProducer producer            = new TransactionMQProducer("please_rename_unique_group_name");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), r -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });

        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();

        rocketMQTemplate.sendMessageInTransaction("seckill-order-pay", message, null);
        return order.getOrderId();
    }

}
