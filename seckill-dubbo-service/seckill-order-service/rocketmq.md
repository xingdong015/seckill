1. rocketmq事务消息原理: https://help.aliyun.com/document_detail/43348.html
2. rocketmq 安装: https://rocketmq.apache.org/docs/quick-start/
3. rocketmq基本原理： http://rocketmq.apache.org/docs/core-concept/
4. rocketmq控制台： https://github.com/apache/rocketmq-dashboard

> producer group概念：     Producers of the same role are grouped together. A different producer instance of the same producer group may be contacted by a broker to commit or roll back a transaction in case the original producer crashed after the transaction.

同一个进程中的consumer Group必须是完全一样的订阅topic, 也就是说同一个consumer消费者实例、如果consumerGroup是相同的、那么他们的topic也必须是想通的。
参考OrderListener.java 和 RocketMQMessageListener的配置
Warning: consumer instances of a consumer group must have exactly the same topic subscription(s).

