package com.system.design.seckill.order.service;

import com.google.common.base.Preconditions;
import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.entity.SeckillOrder;
import com.system.design.seckill.common.enums.SeckillStatusEnum;
import com.system.design.seckill.common.exception.SeckillException;
import com.system.design.seckill.order.mapper.KillMapper;
import com.system.design.seckill.order.mapper.OrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
@DubboService(version = "1.0.0")
public class OrderService implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KillMapper killMapper;

    @Override
    public SeckillOrder createOrder(long skuId, String userId) {
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setSeckillId(skuId);
        seckillOrder.setUserId(userId);
        seckillOrder.setCreateTime(System.currentTimeMillis());
        seckillOrder.setUpdateTime(System.currentTimeMillis());
        seckillOrder.setStatus(0);
        int insert = orderMapper.insert(seckillOrder);
        System.out.println("创建订单成功: " + insert);
        return seckillOrder;
    }

    @Override
    public SeckillOrder getOrderInfo(long orderId) {

        return null;
    }

    @Override
    public void updateOrderStatus(long orderId, String s) {

    }

    /**
     * 从business业务中获取的秒杀数据
     * <p>
     * 执行扣减库存。创建订单等逻辑。提交到rocketMq的延迟队列中去。
     * <p>
     * 分布式事务。事务消息机制
     *
     * @param killId
     * @param userId
     * @return
     *
     * 事务发起方、TM
     */
    @GlobalTransactional
    public Long doKill(long killId, String userId) {
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
        return order.getOrderId();
    }


}
