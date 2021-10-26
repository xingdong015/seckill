package com.system.design.seckill.dubbo;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.system.design.seckill.db.mapper.SeckillMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
@Slf4j
@DubboService(version = "1.0", registry = {"seckill"})
public class StorageServiceConsumer extends ServiceImpl {
    @Resource
    private SeckillMapper seckillMapper;

    @DubboReference
    private StorageServiceConsumer storageServiceConsumer;

    public Integer decreaseStorage(Long killId) {
        return storageServiceConsumer.decreaseStorage(killId);
    }
}
