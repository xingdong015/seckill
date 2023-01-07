package com.xingdong.seckill.order.controller;

import com.xingdong.seckill.order.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/29
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @RequestMapping("/kill")
    @ResponseBody
    public Object createOrder(@RequestParam("skuId") long skuId,@RequestParam("userId") String userId) throws MQClientException {
        return orderService.doKill(skuId, userId);
    }
}
