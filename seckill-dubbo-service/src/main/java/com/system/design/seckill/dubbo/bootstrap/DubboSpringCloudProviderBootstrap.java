package com.system.design.seckill.dubbo.bootstrap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableAutoConfiguration
public class DubboSpringCloudProviderBootstrap {
    /**
     * 在引导 DubboSpringCloudServerBootstrap 之前，
     * 请提前启动 Nacos 服务器。 当 DubboSpringCloudServerBootstrap 启动后，
     * 将应用 seckill-app 将出现在 Nacos 控制台界面。
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudProviderBootstrap.class);

    }

}