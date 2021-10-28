package com.system.design.seckil.common.transaction;

import com.system.design.seckill.common.api.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
@Slf4j
@EnableDiscoveryClient
@Service
public class DecreaseStorageTccService {

    @DubboReference
    private IStockService stockService;

    public Integer decreaseStorage(Long killId) {
        return stockService.decreaseStorage(killId);
    }
}
