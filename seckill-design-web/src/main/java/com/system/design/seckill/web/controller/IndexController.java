package com.system.design.seckill.web.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("v1")
public class IndexController {

    @Autowired
    DefaultMQProducer defaultMQProducer;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("send")
    public String sendMsg() throws UnsupportedEncodingException {
        Message message = new Message("topic_order","hello".getBytes(RemotingHelper.DEFAULT_CHARSET));
        try {
            defaultMQProducer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException |InterruptedException e) {
            e.printStackTrace();
        }
        return "successful";
    }
}
