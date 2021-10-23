package com.system.design.seckill.dbservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
public class SeckillService extends ServiceImpl<SeckillInfoMapper, Seckill> {

    public void deductStock(Long killId) {

        //这里失败的原因如下： 由于有可能消息投递过程中会出现部分失败的情况下，导致redis中扣减了库存而数据库却没有扣减库存
        //所以通常来说会给redis的库存中多放1.5倍的量、这样情况下，进入到mysql的有可能会大于实际的库存量。所以mysql中需要在通过
        //乐观锁的形式来判断库存是否小于0了
        //扣减库存失败。说明已经卖光了、此时无需在给redis的库存+1，直接返回秒杀失败就行。
    }

    public void incCount(long seckillId, int count) {

    }
}
