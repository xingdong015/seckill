package com.system.design.seckil.business;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeckillBusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder providerBuilder = new SpringApplicationBuilder();
        providerBuilder.web(WebApplicationType.NONE)
                .sources(SeckillBusinessServiceApplication.class).run(args);
    }

}
