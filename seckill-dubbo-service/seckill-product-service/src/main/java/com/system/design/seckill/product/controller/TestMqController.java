package com.system.design.seckill.product.controller;

import com.system.design.seckill.product.entity.MyResponse;
import com.system.design.seckill.product.mq.producer.PuTongSendMessage;
import com.system.design.seckill.product.mq.producer.ShunXuSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-22 17:17
 */
@RestController
@RequestMapping("/test/mq")
@CrossOrigin(allowCredentials="true")
@Slf4j
public class TestMqController {
    @Autowired
    private PuTongSendMessage puTongSendMessage;
    private ShunXuSendMessage shunXuSendMessage;

    @GetMapping("/1")
    public void syncSend(){
        puTongSendMessage.SyncSend();
    }

    @GetMapping("/2")
    public void asyncSend(){
        puTongSendMessage.AsyncSend();
    }

    @GetMapping("/3")
    public void oneWaySend(){
        puTongSendMessage.OneWaySend();
    }

    @GetMapping("/4")
    public void sendOrderly(){
        shunXuSendMessage.sendOrderly();
    }

    @GetMapping("/5")
    public void asyncSendOrderly(){
        shunXuSendMessage.asyncSendOrderly();
    }
}
