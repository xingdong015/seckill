package com.system.design.seckill.dubbo;


import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DubboSpringCloudProviderBootstrap {
    /**
     * 在引导 DubboSpringCloudServerBootstrap 之前，
     * 请提前启动 Nacos 服务器。 当 DubboSpringCloudServerBootstrap 启动后，
     * 将应用 seckill-app 将出现在 Nacos 控制台界面。
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboSpringCloudProviderBootstrap.class).web(WebApplicationType.NONE).run(args);
    }

}