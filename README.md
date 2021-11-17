1. druid监控地址： http://127.0.0.1:8082/druid/index.html
2. nacos监控地址:  http://rgb-fk.com:8848/nacos/#/serviceManagement?dataId=&group=&appName=&namespace=&pageSize=&pageNo=
3. 异常处理机制：https://cloud.tencent.com/developer/article/1171987
4. 符合rest风格的api影响结果设计： 参考 Resp<T> 对象
5. MySQL与ES之间的数据一致性问题
    1. https://www.silince.cn/2021/05/15/MySQL%E4%B8%8EES%E4%B9%8B%E9%97%B4%E7%9A%84%E6%95%B0%E6%8D%AE%E4%B8%80%E8%87%B4%E6%80%A7%E9%97%AE%E9%A2%98/
6. dubbo 文档
    1. https://github.com/alibaba/dubbo-spring-boot-starter/blob/master/README_zh.md
    2. spring-cloud-alibaba-dubbo 配置  https://www.iocoder.cn/Spring-Cloud-Alibaba/Dubbo/
7. 在开发阶段由于表结构需要频繁变更、如果有表结构变更需求、引入了 mybatis-enhance-actable 组件、此组件的原理我已经在dubbo-order模块中详细说明、实际就是自动代理了
一些mybatis的功能而已、我已经在dubbo-order中集成完成、如果需要重新生成表只需要修改 common中的实体类、然后运行dubbo-order模块既可以删除然后重新新建表结构。
8. 解决jar包冲突的方案：https://blog.csdn.net/w1014074794/article/details/114668927
   
   