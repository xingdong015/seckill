package com.system.design.seckil.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeckillCommonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillCommonServiceApplication.class, args);
    }

}
