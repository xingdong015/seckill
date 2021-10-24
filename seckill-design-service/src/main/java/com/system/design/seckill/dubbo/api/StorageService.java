package com.system.design.seckill.dubbo.api;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public interface StorageService {

    Integer reduceStock(Long killId);
}
