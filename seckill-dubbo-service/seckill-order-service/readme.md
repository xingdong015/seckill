1. 从rocketmq中接受消息、创建订单、扣减mysql库存
2. 提供订单的增删改查逻辑
3. 投递订单状态到支付系统中。接受支付系统的回调处理。
4. 添加订单超时未支付处理机制。

springboot根目录扫描包路径问题：
https://blog.csdn.net/wojiushiwo945you/article/details/109302986
seata配置流程：
