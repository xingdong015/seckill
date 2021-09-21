package com.system.design.seckill.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.system.design.seckill.bean.PayResultStatus;
import com.system.design.seckill.bean.SeckillResultStatus;
import com.system.design.seckill.service.KillBuzService;
import com.system.design.seckill.service.OrderBuzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private KillBuzService  killBuzService;
    @Autowired
    private OrderBuzService orderBuzService;

    @GetMapping(value = "/list/all")
    public List<Map<String, Object>> getSeckillList() throws JsonProcessingException {
        return killBuzService.getSeckillList();
    }

    @GetMapping(value = "/list")
    public Map<String, Object> getSeckillById(@RequestParam String id) throws JsonProcessingException {
        return killBuzService.getById(id);
    }

    @GetMapping(value = "/execute")
    public SeckillResultStatus executeSeckill(@RequestParam long seckillId, @RequestParam long userId, @RequestParam String md5) {
        return killBuzService.executeKill(seckillId, userId, md5);
    }


    @GetMapping(value = "/order/pay")
    public PayResultStatus payment(@RequestParam long orderId, @RequestParam long userId) {
        return orderBuzService.pay(orderId, userId);
    }
}
