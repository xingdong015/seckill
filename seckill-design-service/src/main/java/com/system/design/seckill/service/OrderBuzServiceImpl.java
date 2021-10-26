package com.system.design.seckill.service;

import com.google.common.base.Preconditions;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.dubbo.OrderServiceConsumer;
import com.system.design.seckill.dubbo.StockServiceConsumer;
import com.system.design.seckill.common.exception.SeckillException;
import com.system.design.seckill.service.api.OrderBuzService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
@Service
public class OrderBuzServiceImpl implements OrderBuzService {
    @Resource
    private OrderServiceConsumer orderService;
    @Resource
    private StockServiceConsumer storageService;

    /**
     * 扣减库存、下单
     *
     * @param killId 秒杀的商品id
     * @param userId 用户id
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public Long doKill(long killId, String userId) {

        int count = storageService.decreaseStorage(killId);
        Preconditions.checkArgument(count >= 1, "%s|%s|库存不足", killId, userId);

        OrderEntity order = orderService.createOrder(killId, userId);
        if (Objects.isNull(order)) {
            throw new SeckillException(String.format("order error => killId:%s userId:%s", killId, userId));
        }
        Preconditions.checkNotNull(order.getOrderId(), "%s|%s|订单创建失败", killId, userId);

        addPayMonitor(order.getOrderId());

        return order.getOrderId();
    }

    private void addPayMonitor(Long orderId) {

    }

}
