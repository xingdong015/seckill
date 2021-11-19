package com.system.design.seckill.product.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-18 18:58
 */
@Slf4j
@Component
public class CanalDataHandleFactory {
    @Resource
    Map<String, CanalDataHandleStrategy> canalDataHandleStrategyMap = new ConcurrentHashMap<>();

    public CanalDataHandleStrategy getCanalDataHandleStrategy(String service){
        CanalDataHandleStrategy canalDataHandleStrategy = canalDataHandleStrategyMap.get(service);
        if (canalDataHandleStrategy == null){
            throw new RuntimeException("no CanalDataHandleStrategy define");
        }
        return canalDataHandleStrategy;
    }

}
