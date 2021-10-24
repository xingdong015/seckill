package com.system.design.seckill.dubbo;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.system.design.seckill.dubbo.api.StorageService;
import com.system.design.seckill.mapper.SeckillMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
@Service
@Slf4j
//@DubboService(version = "1.0",interfaceClass = StorageService.class)
public class StorageServiceImpl extends ServiceImpl implements StorageService {
    @Resource
    private SeckillMapper seckillMapper;

    @Override
    public Integer decreaseStorage(Long killId) {
//        return seckillDao.updateStock(killId);
        return seckillMapper.decreaseStorage(killId);
    }
}
