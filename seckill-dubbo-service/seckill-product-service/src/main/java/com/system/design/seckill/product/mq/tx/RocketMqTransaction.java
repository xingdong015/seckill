package com.system.design.seckill.product.mq.tx;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * @description:
 * 事务消息共有三种状态，提交状态、回滚状态、中间状态：
 * TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。
 * TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。
 * TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。
 * @author: 贾凯
 * @create: 2021-11-24 14:15
 */
@RocketMQTransactionListener
public class RocketMqTransaction implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        //如果此处状态为已提交，则不会走回查方法
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        System.out.println("这是消息内容：" + message);
        return RocketMQLocalTransactionState.COMMIT;
    }
}
