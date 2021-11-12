package com.system.design.seckill.order.controller;

import com.system.design.seckill.order.service.OrderService;
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
    public Object createOrder(@RequestParam("skuId") long skuId,@RequestParam("userId") String userId) {
        return orderService.doKill(skuId,userId);
    }
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world!";
    }

}
