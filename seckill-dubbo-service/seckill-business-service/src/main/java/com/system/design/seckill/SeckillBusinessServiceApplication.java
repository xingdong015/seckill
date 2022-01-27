package com.system.design.seckill;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.system.design.seckill.business.*")
@EnableDiscoveryClient
public class SeckillBusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder providerBuilder = new SpringApplicationBuilder();
//        providerBuilder.web(WebApplicationType.NONE)
//                .sources(SeckillBusinessServiceApplication.class).run(args);
        providerBuilder.sources(SeckillBusinessServiceApplication.class).run(args);
    }

}
