package com.system.design.seckill.service;

import com.system.design.seckill.api.StockInfoService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(registry = {"stock-service-provider-zk"}, version = "1.0.0", group = "dubbo")
public class StockInfoServiceImpl implements StockInfoService {

    @Override
    public void insertStockInfo() {

    }

    @Override
    public void updateStockInfo() {

    }
}
