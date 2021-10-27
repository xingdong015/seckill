package com.system.design.seckill.dubbo.service;

import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@DubboService
public class StockService implements IStockService {

    @Override
    public Integer decreaseStorage(Long killId) {
        System.out.println("扣减库存成功");
        return 1;
    }
}
