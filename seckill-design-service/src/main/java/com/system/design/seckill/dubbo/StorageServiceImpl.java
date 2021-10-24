package com.system.design.seckill.dubbo;

import com.system.design.seckill.dao.SeckillDao;
import com.system.design.seckill.dubbo.api.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
@Service
@Slf4j
@DubboService(version = "1.0",interfaceClass = StorageService.class)
public class StorageServiceImpl implements StorageService {
    @Autowired
    private SeckillDao seckillDao;

    @Override
    public Integer reduceStock(Long killId) {
        return seckillDao.updateStock(killId);
    }
}
