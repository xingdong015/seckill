package com.system.design.seckill.web;

import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.dubbo.service.IOrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengzhengzheng
 * @date 2021/10/27
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@RestController
public class DubboConsumerIndex {
    @DubboReference
    private IOrderService orderService;

    @GetMapping("/create")
    public String createOrder(){
        OrderEntity order = orderService.createOrder(1, "1");
        return String.valueOf(order.getOrderId());
    }

}
