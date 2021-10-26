package com.system.design.seckill.dubbo.service;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
public interface IStockService {
    Integer decreaseStorage(Long killId);
}
