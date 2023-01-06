### 一.模块结构
~~~~
1.controller层为ES中检索product信息入口;    
2.service层中ProductService对外暴露dubbo调用接口;   
3.listener包中是canal-client,进行mysql的binlog日志订阅,将insert、delete、update同步到ES中;   
  同步过程通过batchId加redis分布式锁，避免分布式环境下重复同步,并达到提高消费监听到的信息的效果。
~~~~
### 二.问题
####1)关于product信息的增删改查，统一同步到redis
#####1.存储
~~~~
*项目初始化的时候，全量缓存到redis，然后canal监控增量同步数据到redis
1.prouct的id如何生成？通过redis的string结构自增获取？
2.zset("product-key","id-value",createtime-score);   
  hash("product-id-key","product-field-hkey","peoduct-fieid-hkey-value")   
  存储产品信息，用这样的方式存储达到zset分页排序获取所有id，拿着所有id去hash里面获取产品对象product
~~~~
#####2.缓存预热
~~~~
1.提供redis中所有产品信息curd以及分页检索的接口;
2.提供redis中单个产品信息curd的接口;
~~~~