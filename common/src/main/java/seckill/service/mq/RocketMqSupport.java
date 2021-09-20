package seckill.service.mq;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocketMqSupport {

    @Autowired
    DefaultMQProducer defaultMQProducer;

    public int createTopicId() {
        Message message        = new Message();
        message.setTags("*");
        message.setTopic("hello-");
        message.setBody("ping...".getBytes());
        try {
            defaultMQProducer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
        }
        return HttpResponseStatus.OK.code();
    }
}
