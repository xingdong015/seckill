package com.system.design.seckill.dubbo.service;

import com.system.design.seckill.db.mapper.SeckillMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@DubboService(protocol = "dubbo")
public class StockService implements IStockService {

    @Resource
    private SeckillMapper seckillMapper;

    @Override
    public Integer decreaseStorage(Long killId) {
        System.out.println("扣减库存成功");
        return 1;
    }
}
