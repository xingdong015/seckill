1. 排查dubboReference无法引用过程中的过程？ 问题复现。在web模块中的IndexControler中 @DubboReference(version = "1.0.0") 如果没有添加 version=1.0.0的情况下,因为
account服务是如下方式暴露的。

```java

@DubboService(protocol = {"dubbo"}, version = "1.0.0")
public class AccountService implements IAccountService {
    //。。。。
}
```
根据错误信息可以定位到ReferenceConfig、spring经过ReferenceAnnotationBeanPostProcessor后置处理器的处理之后、会进行初始化DubboReference引用的dubbo服务的客户端的初始化逻辑。具体就是ReferenceConfig的init方法内部。
init方法内部回调用到MockClusterInvoker--->RegistryDirectory。这个类才是注册中心中保留服务地址信息的。此类里面有一个notify方法、经过一步一步的debug源码发现此类是当dubboReference订阅某一个dubbo服务之后
服务端地址有变更的地址推送会调用到这个notify方法之中。
at org.apache.dubbo.config.ReferenceConfig.checkInvokerAvailable(ReferenceConfig.java:420) ~[dubbo-2.7.8.jar:2.7.8]
at org.apache.dubbo.config.ReferenceConfig.init(ReferenceConfig.java:315) ~[dubbo-2.7.8.jar:2.7.8]
at org.apache.dubbo.config.ReferenceConfig.get(ReferenceConfig.java:205) ~[dubbo-2.7.8.jar:2.7.8]
at org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor.doGetInjectedBean(ReferenceAnnotationBeanPostProcessor.java:144) ~[dubbo-2.7.8.jar:2.7.8]
at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor.getInjectedObject(AbstractAnnotationBeanPostProcessor.java:362) ~[spring-context-support-1.0.10.jar:na]
at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor$AnnotatedFieldElement.inject(AbstractAnnotationBeanPostProcessor.java:542) ~[spring-context-support-1.0.10.jar:na]
at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:130) ~[spring-beans-5.2.5.RELEASE.jar:5.2.5.RELEASE]
at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor.postProcessPropertyValues(AbstractAnnotationBeanPostProcessor.java:145) ~[spring-context-support-1.0.10.jar:na]
... 17 common frames omitted


一、 防止刷爆商品页面
1. 未开始抢购时,禁用抢购按钮
如何计算倒计时?

        1. 查询活动表将参与活动的所有产品全部下发给端上。
        2. 端上判断是否展示开始秒杀按钮。
        3. 端上每秒请求一次服务端查看是否秒杀已经开始
        4. 服务端推送秒杀倒计时 websocket 长连接





nginx--->网关---->web层控制器处理逻辑

1. 系统初始化 /index--->返回所有可以参与秒杀的产品列表 (需要有倒计时、标识秒杀的开始时间)
2. 点击任意一个进入详情页面---> /detail
