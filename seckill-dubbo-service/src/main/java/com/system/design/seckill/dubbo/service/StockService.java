package com.system.design.seckill.dubbo.service;

import org.apache.dubbo.config.annotation.DubboService;


/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@DubboService(protocol = "dubbo")
public class StockService implements IStockService {

    @Override
    public Integer decreaseStorage(Long killId) {
        System.out.println("扣减库存成功");
        return 1;
    }
}
