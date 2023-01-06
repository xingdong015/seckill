package com.system.design.seckill.pay.service;


import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.api.IPayService;
import com.system.design.seckill.common.bean.PayResultStatus;
import com.system.design.seckill.common.po.SeckillOrder;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Random;

@DubboService
public class PayBuzServiceImpl implements IPayService {

    @DubboReference(version = "1.0.0")
    private IOrderService orderService;
//    @Autowired
//    private DefaultMQProducer    defaultMQProducer;

    @Override
    public PayResultStatus pay(long orderId, long userId) {
        try {
            SeckillOrder seckillOrderInfo = orderService.getOrderInfo(orderId);
            if (seckillOrderInfo == null) {
                return PayResultStatus.buildOrderExistedException(orderId, userId);
            }
            boolean success = doPay(seckillOrderInfo, userId);
            if (success) {
                orderService.updateOrderStatus(orderId, "1");
                return PayResultStatus.buildSuccessPay(orderId, userId);
            }
//            Message message = new Message();
//            message.setTopic(KillEventTopiEnum.PAY_STATUS_CHANGE.getTopic());
//            JSONObject object = new JSONObject();
//            object.put("orderId", orderId);
//            RocketMqMessageBean bean = new RocketMqMessageBean(object.toJSONString(), -1, System.currentTimeMillis());
//            message.setBody(JSONObject.toJSONString(bean).getBytes(StandardCharsets.UTF_8));
////            defaultMQProducer.send(message);

            return PayResultStatus.buildPayFail(orderId, userId);
        } catch (Throwable e) {
            return PayResultStatus.buildException(orderId, userId);
        }
    }

    private boolean doPay(SeckillOrder seckillOrderInfo, long userId) {
        if (new Random().nextInt(100) < 50) {
            System.out.println(userId + " 支付成功,订单id " + seckillOrderInfo.getOrderId());
            return true;
        }
        System.out.println(userId + " 支付失败,订单id " + seckillOrderInfo.getOrderId());
        return false;
    }
}