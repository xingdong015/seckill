package com.system.design.seckill.web.controller;

import com.system.design.seckill.entity.Seckill;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
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
        } catch (MQClientException | RemotingException |MQBrokerException |InterruptedException e) {
            e.printStackTrace();
        }
        return "successful";
    }


}
