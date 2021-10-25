//package com.system.design.seckill.web.controller;
//
//import com.system.design.seckill.common.entity.Order;
//import com.system.design.seckill.dubbo.OrderServiceConsumer;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.remoting.common.RemotingHelper;
//import org.apache.rocketmq.remoting.exception.RemotingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.io.UnsupportedEncodingException;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("v1")
//public class IndexController {
//
//    @Autowired
//    DefaultMQProducer defaultMQProducer;
//
//    @RequestMapping("/index")
//    public String index() {
//        return "index";
//    }
//
//    @GetMapping("send")
//    public String sendMsg() throws UnsupportedEncodingException {
//        Message message = new Message("topic_order", "hello".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        try {
//            defaultMQProducer.sendOneway(message);
//        } catch (MQClientException | RemotingException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return "successful";
//    }
//
//    @Resource
//    OrderServiceConsumer orderService;
//
//    @GetMapping("test")
//    public String doTest() {
//        Optional<Order> orderOptional = orderService.createOrder(1, "1");
//        System.out.println(orderOptional);
//        return orderOptional.isPresent() ? orderOptional.get().toString() : "error";
//    }
//}
