package com.system.design.seckill.order.listener;

import com.alibaba.fastjson.JSON;
import com.system.design.seckill.common.bean.OrderStatusEnum;
import com.system.design.seckill.common.po.SeckillOrder;
import com.system.design.seckill.order.mapper.KillMapper;
import com.system.design.seckill.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/11/18
 * <p>
 * 支付失败事务消息回调
 */
@RocketMQTransactionListener
@Slf4j
public class PayFailTransactionCallbackListener implements RocketMQLocalTransactionListener {

    @Resource
    private OrderService orderService;


    @Resource
    private KillMapper killMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("订单支付失败，执行更新订单状态以及增加库存 {}", msg);
        //修改订单状态。
        SeckillOrder seckillOrder = JSON.parseObject((byte[]) msg.getPayload(), SeckillOrder.class);
        //更新订单状态为废弃
        orderService.updateOrderStatus(seckillOrder.getOrderId(), String.valueOf(OrderStatusEnum.FAIL.getStatus()));
        log.info("更新订单状态 {} {}", seckillOrder.getOrderId(), OrderStatusEnum.FAIL.getStatus());
        //库存+1  TODO : 模拟如果此操作如果有一个失败了、事务是否会回滚
        killMapper.incStorage(seckillOrder.getSeckillId());
        log.info("增加库存 {}", seckillOrder.getSeckillId());
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
