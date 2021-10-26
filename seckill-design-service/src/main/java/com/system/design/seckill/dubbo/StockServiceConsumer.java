package com.system.design.seckill.dubbo;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.system.design.seckill.db.mapper.SeckillMapper;
import com.system.design.seckill.dubbo.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
@Slf4j
@EnableDiscoveryClient
@Service
public class StockServiceConsumer {

    @DubboReference
    private IStockService stockService;

    public Integer decreaseStorage(Long killId) {
        return stockService.decreaseStorage(killId);
    }
}
